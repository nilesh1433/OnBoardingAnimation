<b>Overview</b>

This library helps in animating drawables from its start position to its end position as defined in the layout.

Usage e.g - OnBoard Screen, Tutorial Screen

<b>Demo</b>

Run sample to see the demo.

<b>Usage</b>


<b>Code details</b>

To leverage this library follow below steps:-

<b>Steps to create layout</b>

<position_var> - Declare a variable to 

X.Position.START(to start animation from start point to end)

X.Position.END(to start animation from end point to start)

In your layout make all views to be animated an instance of MovableView

Call method X.init(<view of fragment or activity>). Store return arrayList in ArrayList<View>. 

Call method X.animate(<position_var>, <movable>, <positionOffset>)



<View of fragment or activity> - Pass root view of activity / inflated view of fragment

<positionOffset> - [0, 1] / [-1, 0] - range of positionOffset











<b>Steps to setup OnBoarding Experience</b>

<b>Activity setup</b>

Create an activity which extends TutorialActivity

Add viewPager to the activity

Add viewPagerAdapter which should be basically group of fragments

Call super.setUpOnBoardAnimation(viewPager, <layout_holder_for_indicator>).

<b>Fragment Setup</b>

Create fragment implementing TutorialFragment

OnCreateView call method super.initOnBoardFragment(<View_of_fragment>)
<br/><br/>

<b>Few pointers while developing layout</b>

Add xmlns schema for the MovableView

while making layout <b>set show_layout_dev = true</b>. <b>Set to false</b> before <b>running on device</b>.

provide start X,Y coordinate using attributes defined.

provide end X,Y coordinate using attributes defined.

set layout_width and layout_height as wrap_content.

set <b>dev_layout_width for specific value</b>, else default is wrap_content

set <b>dev_layout_height for specific value</b>, else default is wrap_content
<br/><br/>


<b>Layout custom attributes details</b>

MovableView custom attributes

startX - start X coordinate

startX_left - set start X coordinate to the left most of layout

startX_right - set start X coordinate to the right most of layout

startX_center_horizontal - set start X coordinate to the center horizontal of layout

startY - start Y coordinate

startY_top - set start Y coordinate to the left most of layout

startY_bottom - set start Y coordinate to the right most of layout

startY_center_vertical - set start Y coordinate to the center horizontal of layout

endX - end X coordinate

endX_left - set end X coordinate to the left most of layout

endX_right - set end X coordinate to the right most of layout

endX_center_horizontal - set end X coordinate to the center horizontal of layout

endY - end Y coordinate

endY_top - set end Y coordinate to the left most of layout

endY_bottom - set end Y coordinate to the right most of layout

endY_center_vertical - set end Y coordinate to the center horizontal of layout

dev_layout_width - set layout width

dev_layout_height - set layout height

show_layout_dev - enables layout viewing while creating layout in proper manner

