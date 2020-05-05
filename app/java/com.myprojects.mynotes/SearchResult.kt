package com.awesome.mystartup

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.BaseAdapter
import android.widget.LinearLayout.VERTICAL
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.notes_ticket.view.*
import java.util.zip.Inflater

class SearchResult : AppCompatActivity() {
    var noteList = ArrayList<Notes>()
    private lateinit var notesSearchAdapter: NotesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        fun doMySearch(title:String,details:String) {

            var dbManager=NotesDBManager(this)
            val projections = arrayOf("ID","Title","Details")
            val selectionArgs = arrayOf(title!!,details!!) // "%" instead of title when you are okay with any/all data
            val cursor = dbManager.Query(projections,"Title like ? or Details like ?",selectionArgs,null)   //values of Selection Args replace the "?"
            noteList.clear()
            if(cursor.moveToFirst()){
                do {
                    val ID = cursor.getInt(cursor.getColumnIndex("ID")) //Passed the index of ID column to getInt. getInt got the ID for us
                    val title = cursor.getString(cursor.getColumnIndex("Title"))
                    val details = cursor.getString(cursor.getColumnIndex("Details"))
                    noteList.add(Notes(ID,title,details))
                }while(cursor.moveToNext())
            }

            initSearchRecyclerView()
            notesSearchAdapter.submitList(noteList)
        }

        if(Intent.ACTION_SEARCH == intent.action){
            intent.getStringExtra(SearchManager.QUERY)?.also { query ->  doMySearch("%"+query+"%","%"+query+"%")}
        }

    }

    fun initSearchRecyclerView(){
        recyclerNotesSearch.apply {
            layoutManager = StaggeredGridLayoutManager(2,VERTICAL)
            notesSearchAdapter = NotesRecyclerAdapter()
            adapter = notesSearchAdapter
        }
    }


    fun EditNote(note:Notes,context: Context?){

        var noteID = note.nodeId
        var noteTitle = note.nodeTitle
        var noteDetails = note.nodeDetails
        var intent = Intent(context,AddNote::class.java)
        intent.putExtra("ID",noteID)
        intent.putExtra("Title",noteTitle)
        intent.putExtra("Details",noteDetails)
        context!!.startActivity(intent)
    }

}
