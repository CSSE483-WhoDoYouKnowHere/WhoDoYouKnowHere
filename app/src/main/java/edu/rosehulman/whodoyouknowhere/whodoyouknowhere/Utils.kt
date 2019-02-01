package edu.rosehulman.whodoyouknowhere.whodoyouknowhere

import java.util.*

object Utils {

    fun getSampleEvents(): Event {
        val sampleEvents: ArrayList<Event> = arrayListOf(
            Event(
                0, "Title1", "1/12/19", "Olympus Mons,Mars", "An arduous journey",
                false, "Party",
                0.0, "",
                0, ArrayList<User>(0)
            ),
            Event(
                0, "Title2", "1/13/19", "Mona Lua,Hawaii", "A fun get-together!",
                false, "Dinner",
                0.0, "",
                0, ArrayList<User>(0)
            ),
            Event(
                0, "Title3", "1/14/19", "Mount Everest,Nepal", "Probably a fun trek",
                false, "Birthday",
                0.0, "",
                0, ArrayList<User>(0)
            )
        )
        return sampleEvents.random()
    }
}
