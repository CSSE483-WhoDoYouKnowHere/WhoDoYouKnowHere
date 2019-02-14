package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import android.content.res.Resources
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

object Utils {

    fun getSampleEvents(): ArrayList<Event> {
        val sampleEvents: ArrayList<Event> = arrayListOf(
            Event(
                "",
                "Title1",
                "1/12/19",
                "Olympus Mons,Mars",
                "http://pluspng.com/img-png/86993-Space-Gallery-15th-Birthday-Party.jpg",
                "An arduous journey",
                false,
                "Party",
                0.0,
                "",
                0,
                HashMap<String, Int>()
            ),
            Event(
                "",
                "Title2",
                "1/13/19",
                "Mona Lua,Hawaii",
                "https://www.rd.com/wp-content/uploads/2017/11/there-s-actually-a-difference-between-dinner-and-supper_440843308-pressmaster-760x506.jpg",
                "A fun get-together!",
                false,
                "Dinner",
                0.0,
                "",
                0,
                HashMap<String, Int>()
            ),
            Event(
                "",
                "Title3",
                "1/14/19",
                "Mount Everest,Nepal",
                "https://vignette.wikia.nocookie.net/ideas/images/e/e4/Movie_night.jpg",
                "Probably a fun trek",
                false,
                "Birthday",
                0.0,
                "",
                0,
                HashMap<String, Int>()
            )
        )
        return sampleEvents
    }

    fun getDisplaySize(windowManager: WindowManager): Point {
        try {
            if (Build.VERSION.SDK_INT > 16) {
                val display = windowManager.defaultDisplay
                val displayMetrics = DisplayMetrics()
                display.getMetrics(displayMetrics)
                return Point(displayMetrics.widthPixels, displayMetrics.heightPixels)
            } else {
                return Point(0, 0)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return Point(0, 0)
        }

    }

    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun getSampleUsers(): ArrayList<User> {
        val sampleUsers: ArrayList<User> = arrayListOf(
            User(
                "", "qd",
                "Anand Desai",
                "https://cdn.pixabay.com/photo/2018/03/31/16/23/african-american-3278519_1280.jpg",
                21,
                "Male",
                0,
                "I like video games and CS"
            ),
            User(
                "", "yfds",
                "Rebecca Kramer",
                "https://www.jottarengenharia.com.br/assets/img/faces/face-19.jpg",
                21,
                "Female",
                0,
                "I enjoy parties and gardening"
            ),
            User(
                "", "lpds",
                "Alex Dripchak",
                "https://cdn.pixabay.com/photo/2017/08/30/17/27/business-woman-2697954_960_720.jpg",
                20,
                "Male",
                0,
                "I'm an outgoing guy who likes to have fun"
            ),
            User(
                "", "dsfdd",
                "Bailey Morgan",
                "https://cdn.pixabay.com/photo/2017/08/30/17/27/business-woman-2697954_960_720.jpg",
                20,
                "Male",
                0,
                "I'm an outgoing guy who likes to have fun"
            )

        )
        return sampleUsers
    }

    fun getSampleEventUrl(): String {
        val sampleUrl = arrayListOf<String>(
            "http://pluspng.com/img-png/86993-Space-Gallery-15th-Birthday-Party.jpg",
            "https://images.pexels.com/photos/1190298/pexels-photo-1190298.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=750&w=1260",
            "https://prods3.imgix.net/images/articles/2016_02/Hero-Dinner-Party.jpg",
            "https://mandndance.com/wp-content/uploads/2018/12/christmas_party.jpg"
        )
        return sampleUrl.random()
    }

    fun getSampleUserUrl(): String {
        val sampleUrl = arrayListOf<String>(
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSlPL_lpxgLe9DusTzQZzye7-a1vRJW_6-M5D_YTKNbTlzlJDLm",
            "https://www.catleylakeman.co.uk/assets/img/CATLEY_LAKEMAN-Russell.jpg",
            "https://www.mckinsey.com/~/media/McKinsey/Careers%20REDESIGN/Meet%20our%20People/Profiles/Shadrack%20Kiratu/shadrack-kiratu_profile_1536x1152.ashx",
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTRtf77G87VfSBB0PvwH_wluKjWY6sLt_4k4LRWgqyoeMHw-1UC"
        )
        return sampleUrl.random()
    }


}
