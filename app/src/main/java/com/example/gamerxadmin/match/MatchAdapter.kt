package com.example.gamerxadmin.match

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.gamerxadmin.R
import com.example.gamerxadmin.models.Match
import com.example.gamerxadmin.utils.getSpotsLeft
import kotlinx.android.synthetic.main.card_match.view.*

class MatchAdapter(var context: Context, var matchList: List<Match>, var isVertical: Boolean = true)
    : RecyclerView.Adapter<MatchAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {


        val itemView = if(isVertical) {
            LayoutInflater.from(context).inflate(R.layout.card_match, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.row_match_item, parent, false)
        }
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return matchList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val match = matchList[position]
        holder.onBindItem(match)

        holder.itemView.setOnClickListener {
            CreateMatch.start(context,match)
        }
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        fun onBindItem(match: Match){
            itemView.card_match_win_amt_tv.text = "₹${match.winAmount}"
            itemView.card_match_map_type_tv.text = "Map: ${match.map}"
            itemView.card_match_spots_left.text = "${getSpotsLeft(match)} spots left"
            itemView.card_match_spots_total.text = "${match.maxPlayers} total spots"
            itemView.card_match_team_type_tv.text = "Team: ${match.teamType}"
            itemView.card_match_players_joined_progress_bar.progress = match.maxPlayers - getSpotsLeft(match)
            itemView.card_match_prize_per_kill_tv.text = "Prize per kill: ₹${match.pricePerKill}"
            itemView.card_match_entry_price_tv.text = "₹${match.entryFee}"
        }
    }


}