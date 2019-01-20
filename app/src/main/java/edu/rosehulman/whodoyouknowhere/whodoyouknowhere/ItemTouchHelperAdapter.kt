package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

interface ItemTouchHelperAdapter {


    fun onItemMove(fromPosition: Int, toPosition: Int)


    fun onItemDismiss(position: Int)
}