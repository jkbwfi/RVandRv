package com.yehwang.recyclerviewandrecyclerview.entity

import com.chad.library.adapter.base.entity.SectionEntity

/**
 * Created by 借力好风 on 2018/9/5.
 * Email:jielihaofeng@aliyun.com
 */

class SectionShopBean : SectionEntity<ShopContentBean> {
    constructor(boolean: Boolean, title: String) : super(boolean, title)
    constructor(t: ShopContentBean) : super(t)
}