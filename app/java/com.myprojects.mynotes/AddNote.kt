package com.awesome.mystartup

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlin.Exception

class AddNote : AppCompatActivity() {

    var title:String? = null
    var details:String? = null
    var ID:Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        try {
            var bundle:Bundle? = intent.extras
            title = bundle!!.getString("Title")
            details = bundle!!.getString("Details")
            ID = bundle!!.getInt("ID")

            etTitleInput.setText(title)
            etDetailsInput.setText(details)

        } catch (ex:Exception){

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        if(ID == null){
            var dbManager = NotesDBManager(this)
            var values = ContentValues()
            values.put("Title",etTitleInput.text.toString())
            values.put("Details",etDetailsInput.text.toString())

            val ID = dbManager.insert(values)
            if(ID>0){
                Toast.makeText(this,"Note is added",Toast.LENGTH_LONG).show()
                finish()
            }
            else{
                Toast.makeText(this,"Note couldn't be added",Toast.LENGTH_LONG).show()
            }
        }

        else{
            var dbManager = NotesDBManager(this)
            var values = ContentValues()

            values.put("Title",etTitleInput.text.toString())
            values.put("Details",etDetailsInput.text.toString())
            var selectionArgs = arrayOf(ID.toString())

            val ID = dbManager.Update(values,"ID=?",selectionArgs)
            if(ID>0){
                Toast.makeText(this,"Note is updated",Toast.LENGTH_LONG).show()
                finish()
            }
            else{
                Toast.makeText(this,"Note couldn't be updated",Toast.LENGTH_LONG).show()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.back_add_note_menu,menu)
        return super.onCreateOptionsMenu(menu)


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu_back -> {

                if(ID == null){
                    var dbManager = NotesDBManager(this)
                    var values = ContentValues()
                    values.put("Title",etTitleInput.text.toString())
                    values.put("Details",etDetailsInput.text.toString())

                    val ID = dbManager.insert(values)
                    if(ID>0){
                        Toast.makeText(this,"Note is added",Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Note couldn't be added",Toast.LENGTH_LONG).show()
                    }
                }

                else{
                    var dbManager = NotesDBManager(this)
                    var values = ContentValues()

                    values.put("Title",etTitleInput.text.toString())
                    values.put("Details",etDetailsInput.text.toString())
                    var selectionArgs = arrayOf(ID.toString())

                    val ID = dbManager.Update(values,"ID=?",selectionArgs)
                    if(ID>0){
                        Toast.makeText(this,"Note is updated",Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else{
                        Toast.makeText(this,"Note couldn't be updated",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }
}
