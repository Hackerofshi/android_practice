package com.sx.ultimatebarx.view


internal class FragmentTag private constructor() : Tag {
    companion object {
        fun getInstance() = Holder.INSTANCE
    }

    private object Holder {
        val INSTANCE = FragmentTag()
    }

    override fun statusBarViewTag(): String {
        return "fragment_status_bar"
    }

    override fun navigationBarViewTag(): String {
        return "fragment_navigation_bar"
    }

}