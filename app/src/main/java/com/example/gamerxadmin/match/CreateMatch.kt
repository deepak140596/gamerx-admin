package com.example.gamerxadmin.match

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import com.example.gamerxadmin.R
import com.example.gamerxadmin.database.createNewMatchToDB
import com.example.gamerxadmin.models.Match
import kotlinx.android.synthetic.main.activity_create_match.*
import java.util.*

class CreateMatch : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_match)

        setupUI()
    }

    private fun setupUI(){
        createMatchBtn.setOnClickListener {
            createMatch()
        }

        createMatchStartTimeEt.setOnClickListener {
            val currentDay = Calendar.getInstance()
            currentDay.timeInMillis = System.currentTimeMillis()
            val datePickerDialog = DatePickerDialog(this,
                this,currentDay.get(Calendar.YEAR),
                currentDay.get(Calendar.MONTH),
                currentDay.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.show()
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, date: Int) {
        calendar.set(year,month,date)
        val timePickerDialog = TimePickerDialog(this,this,15,0,true)
        timePickerDialog.show()
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY,hour)
        calendar.set(Calendar.MINUTE,minute)

        createMatchStartTimeEt.setText(calendar.timeInMillis.toString())
    }

    private fun createMatch(){
        val match = Match()
        match.matchId = createMatchIdEt.text.toString()
        match.startTime = createMatchStartTimeEt.text.toString().toLong()
        match.status = createMatchStatusSpinner.selectedItem.toString()
        match.teamType = createMatchTeamTypeSpinner.selectedItem.toString()
        match.gameId = createMatchGameIdSpinner.selectedItem.toString()
        match.minPlayers = createMatchMinPlayersEt.text.toString().toInt()
        match.maxPlayers = createMatchMaxPlayersEt.text.toString().toInt()
        match.perspectiveMode = createMatchPerspectiveSpinner.selectedItem.toString()
        match.winAmount = createMatchWinAmtEt.text.toString().toDouble()
        match.entryFee = createMatchEntryFeeEt.text.toString().toDouble()
        match.pricePerKill = createMatchPerKillEt.text.toString().toDouble()
        match.map = createMatchMapEt.text.toString()

        createNewMatchToDB(match)
        finish()
    }
}
