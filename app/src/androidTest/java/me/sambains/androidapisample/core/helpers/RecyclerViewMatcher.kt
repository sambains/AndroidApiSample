package me.sambains.androidapisample.core.helpers

import android.content.res.Resources
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

class RecyclerViewMatcher(private val recyclerViewId: Int) {
    fun atPosition(position: Int): Matcher<View> {
        return atPositionOnView(position, -1)
    }

    fun atPositionOnView(position: Int, targetViewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            val resources: Resources? = null
            var childView: View? = null

            override fun describeTo(description: Description) {
                var idDescription = recyclerViewId.toString()
                if (resources != null) {
                    idDescription = try {
                        resources.getResourceName(recyclerViewId)
                    } catch (exception: Resources.NotFoundException) {
                        String.format("%s (resource name not found)", recyclerViewId);
                    }
                }

                description.appendText("RecyclerView with id: $idDescription at position: $position");
            }

            override fun matchesSafely(view: View): Boolean {
                if (childView == null) {
                    val recyclerView = view.rootView.findViewById(recyclerViewId) as RecyclerView
                    if (recyclerView.id == recyclerViewId) {
                        val viewHolder = recyclerView.findViewHolderForAdapterPosition(position) as RecyclerView.ViewHolder
                        childView = viewHolder.itemView
                    } else {
                        return false;
                    }
                }

                return if (targetViewId == -1) {
                    view == childView
                } else {
                    val targetView = childView!!.findViewById(targetViewId) as View
                    view == targetView
                }
            }
        }
    }
}