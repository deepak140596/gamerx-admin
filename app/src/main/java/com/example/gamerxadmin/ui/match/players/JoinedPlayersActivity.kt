package com.example.gamerxadmin.ui.match.players

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamerxadmin.R
import com.example.gamerxadmin.database.getJoinedPlayers
import com.example.gamerxadmin.database.updateMatchStatistics
import com.example.gamerxadmin.models.Match
import com.example.gamerxadmin.models.User
import kotlinx.android.synthetic.main.activity_joined_players.*
import org.jetbrains.anko.startActivity

class JoinedPlayersActivity : AppCompatActivity() {

    companion object{
        private val KEY_SELECTED_MATCH = "selected_match"
        fun start(context: Context, match: Match){
            context.startActivity<JoinedPlayersActivity>(KEY_SELECTED_MATCH to match)
        }
    }

    lateinit var playerAdapter: UpdatePlayerAdapter
    var playerList : List<User> = emptyList()
    lateinit var match : Match

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_joined_players)

        match = intent.getSerializableExtra(KEY_SELECTED_MATCH) as Match
        setupUI()
        getAllPlayers()
    }

    private fun setupUI(){
        joinedPlayersRv.layoutManager = LinearLayoutManager(this,
           LinearLayoutManager.VERTICAL,false)

        playerAdapter = UpdatePlayerAdapter(this,playerList,match)
        joinedPlayersRv.adapter = playerAdapter


        joinedPlayersDoneFAB.setOnClickListener {
//            for(user in playerAdapter.playersList) {
//                log(D,"Name: ${user.name}  Kills: ${user.kills}  Money Earned: ${user.moneyEarned}")
//            }
            joinedPlayersPB.visibility = View.VISIBLE
            updateMatchStatistics(playerAdapter.playersList,match)
            finish()
        }
    }

    private fun getAllPlayers(){
        getJoinedPlayers(match)
            .observe(this, Observer {
                playerList = it
                playerAdapter.playersList = playerList
                playerAdapter.notifyDataSetChanged()
            })
    }
}
