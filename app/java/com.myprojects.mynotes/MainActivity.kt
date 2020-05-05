package com.awesome.mystartup

import android.app.SearchManager
import android.widget.SearchView
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.SearchView.*
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {
    var noteList = ArrayList<Notes>()
    private lateinit var notesRecyclerAdapter:NotesRecyclerAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadQuery("%", "%")



        fabAddNote.setOnClickListener{
            var intent = Intent(this,AddNote::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%","%")
    }

    fun LoadQuery(title:String,details:String){  //If I only take title as input it only searches for titles using input, not description
        var dbManager=NotesDBManager(this)
        val projections = arrayOf("ID","Title","Details")
        val selectionArgs = arrayOf(title,details) // "%" instead of title when you are okay with any/all data
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

        initRecyclerView()
        notesRecyclerAdapter.submitList(noteList)
    }

    fun initRecyclerView(){
        recycler_view.apply {
            layoutManager = StaggeredGridLayoutManager(2, VERTICAL)
            notesRecyclerAdapter = NotesRecyclerAdapter(this@MainActivity)
            adapter = notesRecyclerAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val sView = menu!!.findItem(R.id.appBarSearch).actionView as SearchView
        val sManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sView.setSearchableInfo(sManager.getSearchableInfo(ComponentName(this,SearchResult::class.java)))
        return super.onCreateOptionsMenu(menu)
    }

}
