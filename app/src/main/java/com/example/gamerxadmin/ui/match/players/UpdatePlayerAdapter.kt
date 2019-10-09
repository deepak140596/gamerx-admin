package com.example.gamerxadmin.ui.match.players

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamerxadmin.R
import com.example.gamerxadmin.models.*
import kotlinx.android.synthetic.main.row_player.view.*

class UpdatePlayerAdapter(var context: Context,
                          var playersList: List<User>,
                          var match: Match)
    : RecyclerView.Adapter<UpdatePlayerAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.row_player,parent, false)
        return MyViewHolder(itemView,match)
    }

    override fun getItemCount(): Int {
        return playersList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val player = playersList[position]
        holder.setIsRecyclable(false)
        holder.onBindItem(player)
    }

    class MyViewHolder(itemView: View, var match: Match): RecyclerView.ViewHolder(itemView){

        fun onBindItem(player: User){
            itemView.rowPlayerInGameIdTv.text = player.inGameName
            itemView.rowPlayerNameTv.text = player.name
            itemView.rowPlayerPhoneNumberTv.text = player.phoneNumber

            itemView.rowPlayerPlayersKilledET.addTextChangedListener(object : TextWatcher{
                override fun afterTextChanged(p0: Editable?) {
                    if(p0?.length != 0){
                        player.kills = p0.toString().toInt()
                    } else {
                        player.kills = 0
                    }

                    updateMoneyEarnedTv(player)
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })

            itemView.rowPlayerIsWinnerCB.setOnCheckedChangeListener { _, isWinner ->

                player.isWinner = isWinner
                updateMoneyEarnedTv(player)
            }
        }

        fun updateMoneyEarnedTv(player: User){
            var moneyEarned : Double = (player.kills ?: 0) * match.pricePerKill
            if(player.isWinner){
                when(match.teamType){
                    SOLO->{
                        moneyEarned += match.winAmount
                    }
                    DUO ->{
                        moneyEarned += (match.winAmount / 2)
                    }
                    SQUAD -> {
                        moneyEarned += (match.winAmount / 4)
                    }
                }

            }

            player.moneyEarned = moneyEarned

            itemView.rowPlayerMoneyEarnedTv.text = "₹ $moneyEarned"
        }
    }
}