package com.example.gamerxadmin.match

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gamerxadmin.R
import com.example.gamerxadmin.database.getMatches
import com.example.gamerxadmin.models.Match
import com.example.gamerxadmin.models.UPCOMING
import kotlinx.android.synthetic.main.activity_upcoming_matches.*

class UpcomingMatches : AppCompatActivity() {
    lateinit var matchAdapter: MatchAdapter
    var matchesList: List<Match> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_matches)

        setupRecyclerView()
        getUpcomingMatches()
    }

    private fun setupRecyclerView(){
        upcomingMatchesRV.layoutManager = LinearLayoutManager(this,
            LinearLayoutManager.VERTICAL,false)

        matchAdapter = MatchAdapter(this, matchesList)
        upcomingMatchesRV.adapter = matchAdapter

    }

    private fun getUpcomingMatches(){
        getMatches(UPCOMING).observe(this, Observer {
            matchesList = it
            matchAdapter.matchList = it
            matchAdapter.notifyDataSetChanged()
        })
    }
}
