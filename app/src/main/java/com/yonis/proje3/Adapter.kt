package com.gtappdevelopers.kotlingfgproject

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.yonis.proje3.CardRVModal
import com.yonis.proje3.R
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

// on below line we are creating
// a course rv adapter class.
class Adapter(
    private val courseList: ArrayList<CardRVModal>?,
    private val context: Context,
    var id: Int = 0,
    var id1: Int = 0,
    val clickedCard: (index: Int, id: Int, id1: Int) -> Unit
) : RecyclerView.Adapter<Adapter.CourseViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Adapter.CourseViewHolder {
        // this method is use to inflate the layout file
        // which we have created for our recycler view.
        // on below line we are inflating our layout file.
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.course_rv_item,
            parent, false
        )
        // at last we are returning our view holder
        // class with our item View File.
        return CourseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: Adapter.CourseViewHolder, position: Int) {

        // on below line we are setting data to our text view and our image view.
        //holder.courseNameTV.text = courseList.get(position).courseName
        var temp: Int = -1
        var cardStat: Int = 0
        courseList?.get(position)?.courseImg?.let { holder.courseIV.setImageResource(it) }
        if (!courseList?.get(position)?.status!!) {
            holder.itemView.setOnClickListener {
                if (courseList.get(position).status) {
                    return@setOnClickListener
                }
                holder.courseIV.setImageBitmap(courseList?.get(position)?.courseImg1)
                courseList?.get(position)?.status = true
                if (id == 0) {
                    id = courseList?.get(position)?.id!!.toInt()
                    cardStat = position
                } else if (id1 == 0) {
                    id1 = courseList?.get(position)?.id!!.toInt()

                    clickedCard(position, id, id1)
                    control()

                }

            }
        } else {
            holder.courseIV.setImageBitmap(courseList?.get(position)?.courseImg1)
        }
    }

    fun control() {
        if (id != id1) {
            courseList?.filter {
                it.id?.toInt() ?: 0 == id || it.id?.toInt() ?: 0 == id1
            }?.forEach {
                it.status = false
            }
            println(courseList)
            runAfterDelay {
                notifyDataSetChanged()
            }
        }
        id = 0
        id1 = 0
    }

    fun runAfterDelay(callback: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(1000L)
            withContext(Dispatchers.Main){
                callback()
            }
        }
    }

    override fun getItemCount(): Int {
        // on below line we are
        // returning our size of our list
        return courseList?.size ?: 0
    }

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // on below line we are initializing our course name text view and our image view.
        val courseIV: ImageView = itemView.findViewById(R.id.idIVCourse)
    }
}
