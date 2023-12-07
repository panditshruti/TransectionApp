package com.shrutipandit.homeworkpractice

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Transaction
import com.google.firebase.database.ValueEventListener
import com.shrutipandit.homeworkpractice.databinding.ActivityMainBinding
import com.shrutipandit.homeworkpractice.list.Transection

import java.util.Random

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db:DatabaseReference
    private lateinit var transectionArray:ArrayList<Transection>
    private lateinit var transectionArray2:ArrayList<Transection>
    private lateinit var transectionAdapter:ArrayAdapter<Transection>
    private lateinit var transectionAdapter2:ArrayAdapter<Transection>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseDatabase.getInstance().reference.child("Transaction")

        fetchdata()
        transectionArray = arrayListOf()
        transectionArray2 = arrayListOf()
        transectionAdapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,transectionArray)
        transectionAdapter2 = ArrayAdapter(this,android.R.layout.simple_list_item_1,transectionArray2)

        binding.listview.adapter = transectionAdapter
        binding.listview2.adapter = transectionAdapter2

        binding.floatactionBtn.setOnClickListener {
            transactionAlertBox(name=String() ,amount = String())
        }
    }
    fun transactionAlertBox(name:String,amount:String) {
        val alertDialogBuilder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.sample_transaction, null)

        val name = dialogView.findViewById<EditText>(R.id.nameEd)
        val amount = dialogView.findViewById<EditText>(R.id.amountEd)

        alertDialogBuilder.setView(dialogView)

        alertDialogBuilder.setTitle("Alert Dialog")
        alertDialogBuilder.setMessage("This is a simple alert dialog in Android!")

        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->


            saveDataDb(name.text.toString(),amount.text.toString())

            dialog.dismiss()
        }
        alertDialogBuilder.setNegativeButton("Cancel") { dialog, which ->

            dialog.dismiss()
        }

        // Create and show the dialog
        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    fun saveDataDb(name: String,amount: String){
       db.push().setValue(Transection(name,amount))

}
    fun fetchdata (){

        db.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                transectionArray.clear()
               if (snapshot.exists()){
                   for (data in snapshot.children){
                      val name = data.child("name").value as String
                      val amount = data.child("amount").value as String
                       if(amount =="3000"){
                           transectionArray.add(Transection(name,amount))
                       }
                       else{
                           transectionArray2.add(Transection(name,amount))
                       }
                   }
                   transectionAdapter.notifyDataSetChanged()
                   transectionAdapter2.notifyDataSetChanged()
               }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}


