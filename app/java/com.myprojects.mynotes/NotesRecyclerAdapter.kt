package com.awesome.mystartup

import android.content.Context
import android.content.Intent
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.notes_ticket.view.*

class NotesRecyclerAdapter(): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var noteList = ArrayList<Notes>()
    private lateinit var context:Context

    constructor(context: Context):this(){
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notes_ticket, null))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is NoteViewHolder -> {
                holder.bind(noteList[position])

                holder.itemView.setOnClickListener{
                    editNote(noteList[position])
                }

                holder.itemView.setOnLongClickListener{

                    var actionMode: ActionMode? = null
                    var count:Int? = null
                    val actionModeCallback = object: ActionMode.Callback{
                        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                            return when(item!!.itemId){
                                R.id.menu_delete -> {
                                    var db = NotesDBManager(context)
                                    var selectionArgs = arrayOf(noteList[position].nodeId.toString()) //Confirm it ???
                                    var selection = "ID=?"
                                    count = db.Delete(selection,selectionArgs)

                                    //Close the CAB = Contextual Action Button
                                    mode!!.finish()
                                    val projections = arrayOf("ID","Title","Details")
                                    val newSelectionArgs = arrayOf("%","%")
                                    val cursor = db.Query(projections,"Title like ? or Details like ?",newSelectionArgs,null)

                                    noteList.clear()
                                    if(cursor.moveToFirst()){
                                        do {
                                            val ID = cursor.getInt(cursor.getColumnIndex("ID")) //Passed the index of ID column to getInt. getInt got the ID for us
                                            val title = cursor.getString(cursor.getColumnIndex("Title"))
                                            val details = cursor.getString(cursor.getColumnIndex("Details"))
                                            noteList.add(Notes(ID,title,details))
                                        }while(cursor.moveToNext())
                                    }
                                    notifyDataSetChanged()
                                    if(null != count){
                                        Toast.makeText(context,"Note deleted",Toast.LENGTH_LONG).show()
                                    }

                                    true
                                }

                                else -> {
                                    false
                                }
                            }
                        }

                        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                            val inflater: MenuInflater = mode!!.menuInflater
                            inflater.inflate(R.menu.contextual_action_bar,menu)
                            return true
                        }

                        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                            return false
                        }

                        override fun onDestroyActionMode(mode: ActionMode?) {
                            actionMode = null
                        }

                    }


                    //____________________________________________________________________________
                    when(actionMode){
                        null -> {
                            actionMode = holder.itemView.startActionMode(actionModeCallback)
                            holder.itemView.tvNoteTitle.isSelected = true
                            true
                        }

                        else -> false
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun submitList(noteList:ArrayList<Notes>){
        this.noteList = noteList
    }

    class NoteViewHolder(var noteView:View):RecyclerView.ViewHolder(noteView){
        fun bind(note:Notes){
            noteView.tvNoteTitle.setText(note.nodeTitle)
            noteView.tvNoteDetails.setText(note.nodeDetails)
        }
    }

    fun editNote(note:Notes){
        var noteID = note.nodeId
        var noteTitle = note.nodeTitle
        var noteDetails = note.nodeDetails
        var intent = Intent(context,AddNote::class.java)
        intent.putExtra("ID",noteID)
        intent.putExtra("Title",noteTitle)
        intent.putExtra("Details",noteDetails)
        context.startActivity(intent)
    }
}
