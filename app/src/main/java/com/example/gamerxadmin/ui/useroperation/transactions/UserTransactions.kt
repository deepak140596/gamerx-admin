package com.example.gamerxadmin.ui.useroperation.transactions

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamerxadmin.R
import com.example.gamerxadmin.database.createNewTransactionByPhoneNumber
import com.example.gamerxadmin.database.getTransactionByPhoneNumber
import com.example.gamerxadmin.models.Transaction
import kotlinx.android.synthetic.main.activity_user_transactions.*
import kotlinx.android.synthetic.main.dialog_new_transaction.view.*

class UserTransactions : AppCompatActivity() {

    lateinit var transactionAdapter: TransactionAdapter
    var listTransactions : List<Transaction> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_transactions)

        setupUI()
    }

    private fun setupUI(){
        activity_transactions_rv.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        transactionAdapter = TransactionAdapter(this,listTransactions)
        activity_transactions_rv.adapter = transactionAdapter

        user_transaction_search_btn.setOnClickListener {
            val phoneNumber = "+91${user_transaction_enter_phone_tiet.text.toString()}"
            getTransactions(phoneNumber)
        }

        user_new_transaction_btn.setOnClickListener {
            val phoneNumber = "+91${user_transaction_enter_phone_tiet.text.toString()}"
            showCreateTransactionDialog(phoneNumber)
        }

    }

    private fun getTransactions(phoneNumber: String){
        getTransactionByPhoneNumber(this,phoneNumber).observe(this, Observer { list ->
            list?.let {
                transactionAdapter.transactionList = list
                transactionAdapter.notifyDataSetChanged()
            }

        })
    }

    private fun showCreateTransactionDialog(phoneNumber: String){
        val alertDialog = AlertDialog.Builder(this)
        val view : View = layoutInflater.inflate(R.layout.dialog_new_transaction,null)
        alertDialog.setView(view)
        alertDialog.setTitle("Create Transaction")
        alertDialog.setPositiveButton("Create") { _, _ ->
            val amt = view.new_transaction_amt_tiet.text.toString().toDouble()
            val transactedFor = view.new_transaction_transacted_for_tiet.text.toString()
            val type = view.new_transaction_type_spinner.selectedItem.toString()

            val transaction = Transaction()
            transaction.transactionId = System.currentTimeMillis().toString()
            transaction.amount = amt
            transaction.transactedFor = transactedFor
            transaction.type = type

            createNewTransactionByPhoneNumber(phoneNumber, transaction)
        }.setNegativeButton("Cancel"){_,_ ->}

        alertDialog.show()
    }
}
