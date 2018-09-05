package com.yehwang.recyclerviewandrecyclerview.adapter

import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yehwang.recyclerviewandrecyclerview.R
import java.util.*

/**
 * Created by 借力好风 on 2018/7/30.
 * Email:jielihaofeng@aliyun.com
 */
class SearchLeftListQuickAdapter(data: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_p_search_category_left, data) {

    private val tv = ArrayList<TextView>()


     fun selectItem(position: Int) {

        tv.forEachIndexed { index, _ ->
            if (position == index) {
                tv[index].setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent))
                tv[index].setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
            } else {
                tv[index].setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
                tv[index].setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorAccent))
            }
        }
    }

    override fun convert(helper: BaseViewHolder, item: String) {

        helper.setText(R.id.tv_title, item)
        tv.add(helper.getView(R.id.tv_title))
        //设置进入页面之后,左边列表的初始状态
        if (tv.size == data.size) {
            selectItem(0)
        }


    }


}