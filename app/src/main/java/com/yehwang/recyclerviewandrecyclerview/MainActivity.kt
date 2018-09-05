package com.yehwang.recyclerviewandrecyclerview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import com.yehwang.recyclerviewandrecyclerview.adapter.SearchLeftListQuickAdapter
import com.yehwang.recyclerviewandrecyclerview.adapter.SearchSectionQuickAdapter
import com.yehwang.recyclerviewandrecyclerview.entity.SectionShopBean
import com.yehwang.recyclerviewandrecyclerview.entity.ShopContentBean
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    //左边数据源
    private val mLeftList = mutableListOf("第一组", "第二组", "第三组", "第四组", "第五组", "第六组", "第七组", "第八组", "第九组", "第十组")
    //右边数据源
    private val mRightList: MutableList<SectionShopBean> = mutableListOf()

    //右侧title在数据中所对应的position集合
    private var tPosition = arrayListOf<Int>()

    //title的高度
    private var tHeight: Int = 0

    //记录右侧当前可见的第一个item的position
    private var first = 0

    private val mSearchLeftListQuickAdapter by lazy { SearchLeftListQuickAdapter(mLeftList) }

    private val mSearchSectionQuickAdapter by lazy { SearchSectionQuickAdapter(R.layout.item_p_search_section_content, R.layout.item_p_search_section_head, mutableListOf()) }

    private val mLinearLayoutManager by lazy { LinearLayoutManager(this, OrientationHelper.VERTICAL, false) }

    private val mShopLinearLayoutManager by lazy { LinearLayoutManager(this, OrientationHelper.VERTICAL, false) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSampleData()

        initLeftRecycleView()
        initRightRecyclerView()
    }

    /**
     *设置数据
     */
    private fun setSampleData() {

        mLeftList.forEachIndexed { index, s ->

            mRightList.add(SectionShopBean(true, mLeftList[index]))
            mRightList.add(SectionShopBean(ShopContentBean("借力好风$index", s)))
        }

        mRightList.forEachIndexed { index, sectionShopBean ->
            //遍历右侧列表，判断如果是header，则将此header在右侧列表所在的position添加到集合中
            if (mRightList[index].isHeader) {
                tPosition.add(index)
            }
        }


    }


    private fun initLeftRecycleView() {

        /*左边recyclerview*/
        mSelectedRecyclerView.apply {
            layoutManager = mLinearLayoutManager
            adapter = mSearchLeftListQuickAdapter
        }
        mSearchLeftListQuickAdapter.setNewData(mLeftList)

        mSearchLeftListQuickAdapter.setOnItemClickListener { adapter, view, position ->

            mSearchLeftListQuickAdapter.selectItem(position)
            moveToCenter(position)
            mShopLinearLayoutManager.scrollToPositionWithOffset(tPosition[position], 0)
        }
    }

    private fun initRightRecyclerView() {

        mShopRecyclerView.apply {
            layoutManager = mShopLinearLayoutManager
            adapter = mSearchSectionQuickAdapter
        }
        mSearchSectionQuickAdapter.setNewData(mRightList)

        //设置右侧初始title
        if (mRightList[first].isHeader) {
            right_title.text = mRightList[first].header
        }

        smoothMoveToPosition()

    }
    /**
     *右侧联动
     * 选择正确的tab
     */
    private fun smoothMoveToPosition() {

        mShopRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (mRightList[first].isHeader) {
                    val view = mShopLinearLayoutManager.findViewByPosition(first)
                    if (view != null) {
                        //如果此组名item顶部和父容器顶部距离大于等于title的高度,则设置偏移量
                        if (view.top >= tHeight) {
                            right_title.y = (view.top - tHeight).toFloat()
                        } else {
                            //否则不设置
                            right_title.y = 0f
                        }
                    }
                }
                //因为每次滑动之后,右侧列表中可见的第一个item的position肯定会改变,并且右侧列表中可见的第一个item的position变换了之后,
                //才有可能改变右侧title的值,所以这个方法内的逻辑在右侧可见的第一个item的position改变之后一定会执行
                val firstPosition = mShopLinearLayoutManager.findFirstVisibleItemPosition()

                if (first != firstPosition && firstPosition >= 0) {

                    //给FiRSTFU赋值
                    first = firstPosition
                    //不设置Y轴的偏移量
                    right_title.y = 0f

                    //判断如果右侧可见的第一个item是否是header，设置相应的值
                    if (mRightList[first].isHeader) {
                        right_title.text = mRightList[first].header
                    } else {
                        right_title.text = mRightList[first].t.name
                    }
                }

                /**
                 *遍历左边列表，列表对应的内容等于右边的title，则设置左侧对应的item高亮
                 */
                mLeftList.forEachIndexed { index, s ->
                    if (mLeftList[index] == right_title.text) {

                        mSearchLeftListQuickAdapter.selectItem(index)
                        moveToCenter(index)
                    }
                }
                //如果右边最后一个完全显示的item的position,等于bean中最后一条数据的position(也就是右侧列表拉到底了),
                //则设置左侧列表最后一条item高亮
                if (mShopLinearLayoutManager.findLastCompletelyVisibleItemPosition() == mRightList.size - 1) {
                    mSearchLeftListQuickAdapter.selectItem(mLeftList.size - 1)
                    moveToCenter(mLeftList.size - 1)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //获取右侧title的高度
                tHeight = right_title.height
            }
        })

    }

    //将当前选中的item居中,如果不加这段代码，左边的recyclerview并不会跟随滚动
    private fun moveToCenter(position: Int) {
        //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
        val childAt = mSelectedRecyclerView.getChildAt(position - mLinearLayoutManager.findFirstVisibleItemPosition())
        if (childAt != null) {
            val y = childAt.top - mSelectedRecyclerView.height / 2
            mSelectedRecyclerView.smoothScrollBy(0, y)
        }

    }
}
