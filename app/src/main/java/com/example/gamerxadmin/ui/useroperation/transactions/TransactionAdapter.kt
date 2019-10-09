package com.example.gamerxadmin.ui.useroperation.transactions

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamerxadmin.R
import com.example.gamerxadmin.models.TYPE_CREDIT
import com.example.gamerxadmin.models.TYPE_DEBIT
import com.example.gamerxadmin.models.Transaction
import com.example.gamerxadmin.utils.getHHMMDDDMMM
import kotlinx.android.synthetic.main.row_transaction.view.*

class TransactionAdapter(var context: Context, var transactionList: List<Transaction>)
    : RecyclerView.Adapter<TransactionAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.row_transaction,parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val match = transactionList[position]
        holder.onBindItem(context,match)

    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun onBindItem(context: Context,transaction: Transaction){
            itemView.row_transaction_id_tv.text = "Transaction ID: ${transaction.transactionId}"
            itemView.row_transaction_title_tv.text = transaction.transactedFor
            itemView.row_transaction_date_tv.text = getHHMMDDDMMM(transaction.date)
            itemView.row_transaction_amount_tv.text = "₹ ${transaction.amount}"

            if(transaction.type == TYPE_CREDIT){
                itemView.row_transaction_type_tv.setTextColor(context.getColor(R.color.primary_green))
                itemView.row_transaction_type_iv.setImageResource(R.drawable.ic_account_balance_wallet_black_24dp)
            } else if(transaction.type == TYPE_DEBIT){
                itemView.row_transaction_type_tv.setTextColor(context.getColor(R.color.red))
                itemView.row_transaction_type_iv.setImageResource(R.drawable.ic_gamepad)
            }

            itemView.row_transaction_type_tv.text = transaction.type
        }
    }


}