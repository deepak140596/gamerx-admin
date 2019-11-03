package com.example.gamerxadmin.ui.match

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.lifecycle.Observer
import com.example.gamerxadmin.R
import com.example.gamerxadmin.database.createNewMatchToDB
import com.example.gamerxadmin.database.getJoinedPlayers
import com.example.gamerxadmin.database.setRoomIdPw
import com.example.gamerxadmin.database.updateMatch
import com.example.gamerxadmin.models.Match
import com.example.gamerxadmin.models.RoomCredentials
import com.example.gamerxadmin.utils.createRoomAlert
import com.example.gamerxadmin.utils.getPositionInArray
import kotlinx.android.synthetic.main.activity_create_match.*
import kotlinx.android.synthetic.main.dialog_room_credentials.view.*
import org.jetbrains.anko.startActivity
import java.util.Calendar

class CreateMatch : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener{

    companion object{
        private val KEY_CREATE_MATCH = "create_match"
        fun start(context: Context, match: Match){
            context.startActivity<CreateMatch>(KEY_CREATE_MATCH to match)
        }
    }

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_match)

        val match = intent.extras?.getSerializable(KEY_CREATE_MATCH) as? Match
        match?.let {
            updateUI(it)
        }
        setupUI()
    }

    private fun setupUI(){
        createMatchBtn.setOnClickListener {
            createMatch()
        }

        updateMatchBtn.setOnClickListener {
            createMatch(true)
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

    private fun updateUI(match: Match){
        createMatchIdEt.setText(match.matchId)
        createMatchIdEt.isEnabled = false
        createMatchStartTimeEt.setText(match.startTime.toString())
        createMatchMinPlayersEt.setText("${match.minPlayers}")
        createMatchMaxPlayersEt.setText("${match.maxPlayers}")
        createMatchWinAmtEt.setText("${match.winAmount}")
        createMatchEntryFeeEt.setText("${match.entryFee}")
        createMatchPerKillEt.setText("${match.pricePerKill}")
        createMatchMapEt.setText(match.map)
        createMatchRequestCodeEt.setText("${match.requestCode}")
        createMatchRequestCodeEt.isEnabled = false

        createMatchPerspectiveSpinner.setSelection(getPositionInArray(match.perspectiveMode,
            resources.getStringArray(R.array.perspective_mode)))

        createRoomCredBtn.setOnClickListener {
            showCreateRoomDialog(match)
        }

        createMatchStatusSpinner.setSelection(getPositionInArray(match.status,
            resources.getStringArray(R.array.match_status)))
        createMatchTeamTypeSpinner.setSelection(getPositionInArray(match.teamType,
            resources.getStringArray(R.array.team_type)))
        createMatchGameIdSpinner.setSelection(getPositionInArray(match.gameId,
            resources.getStringArray(R.array.game_id)))

        createMatchBtn.visibility = View.GONE
        updateMatchBtn.visibility = View.VISIBLE
        createRoomCredBtn.visibility = View.VISIBLE
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

    private fun createMatch(update: Boolean = false ){
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
        match.requestCode = createMatchRequestCodeEt.text.toString().toInt()

        if(update){
            updateMatch(match)
        } else {
            createNewMatchToDB(match)
        }
        finish()
    }

    private fun showCreateRoomDialog(match: Match){
        val alertDialog = AlertDialog.Builder(this)
        val view : View = layoutInflater.inflate(R.layout.dialog_room_credentials,null)
        alertDialog.setView(view)
        alertDialog.setTitle("Create Room")
        alertDialog.setPositiveButton("Create") { _, _ ->
            val roomId = view.room_credentials_id_tiet.text.toString()
            val password = view.room_credentials_password_tiet.text.toString()
            val credentials = RoomCredentials(roomId, password)

            setRoomIdPw(match,credentials)
            sendAlertMessageToPlayers(match,credentials)
        }.setNegativeButton("Cancel"){_,_ ->}

        alertDialog.show()
    }

    private fun sendAlertMessageToPlayers(match: Match, credentials: RoomCredentials) {
        getJoinedPlayers(match).observe(this, Observer {
            if(it.isNotEmpty()) {
                createRoomAlert( credentials, it)
            }
        })
    }

}
