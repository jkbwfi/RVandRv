package com.yehwang.recyclerviewandrecyclerview.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseSectionQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.yehwang.recyclerviewandrecyclerview.R
import com.yehwang.recyclerviewandrecyclerview.entity.SectionShopBean


/**
 * Created by 借力好风 on 2018/8/27.
 * Email:jielihaofeng@aliyun.com
 */
class SearchSectionQuickAdapter(layoutResId: Int, sectionHeadResId: Int, data: MutableList<SectionShopBean>?) : BaseSectionQuickAdapter<SectionShopBean, BaseViewHolder>(layoutResId, sectionHeadResId, data) {

    private var selectedPosition = 0

    fun setSelected(position: Int) {
        selectedPosition = position
    }

    override fun convertHead(helper: BaseViewHolder?, item: SectionShopBean?) {
        helper?.getView<TextView>(R.id.tv_title)?.text = item?.header
    }

    override fun convert(helper: BaseViewHolder?, item: SectionShopBean?) {

    }
}