package com.example.formapp

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity

import com.example.formapp.ui.theme.DbHelper
import com.example.formapp.ui.theme.UserData
import org.json.JSONArray

class MainActivity : ComponentActivity() {

    private lateinit var nameEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var bioEditText: EditText
    private lateinit var maleCheckBox: CheckBox
    private lateinit var femaleCheckBox: CheckBox
    private lateinit var skill1Box: CheckBox
    private lateinit var skill2Box: CheckBox
    private lateinit var skill3Box: CheckBox
    private lateinit var skill4Box: CheckBox
    private lateinit var skillEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var showAllButton: Button
    private lateinit var backbtn: Button
    private lateinit var userDataLayout: View
    private lateinit var userDetailsLayout: View
    private lateinit var userDetailsTextView: TextView

    private lateinit var dbHelper: DbHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        dbHelper = DbHelper(this)


        userDataLayout = findViewById(R.id.userDataLayout)
        userDetailsLayout = findViewById(R.id.userDetailsLayout)
        userDetailsTextView = findViewById(R.id.userDetailsTextView)
        showAllButton = findViewById(R.id.gobtn)
        backbtn = findViewById(R.id.backbtn)

        nameEditText = findViewById(R.id.nameEditTextText)
        phoneEditText = findViewById(R.id.phoneEditText)
        ageEditText = findViewById(R.id.ageEditText)
        bioEditText = findViewById(R.id.bioEditText)
        maleCheckBox = findViewById(R.id.maleCheckBox)
        femaleCheckBox = findViewById(R.id.femaleCheckBox)
        skill1Box = findViewById(R.id.skill1Box)
        skill2Box = findViewById(R.id.skill2Box)
        skill3Box = findViewById(R.id.skill3Box)
        skill4Box = findViewById(R.id.skill4Box)
        skillEditText = findViewById(R.id.skillEditText)
        submitButton = findViewById(R.id.submitbtn)
        showAllButton = findViewById(R.id.gobtn)

        submitButton.setOnClickListener{
            saveUserData()
        }

        showAllButton.setOnClickListener{
            showUserDetails()
        }

        backbtn.setOnClickListener{
            userDataLayout.visibility = View.VISIBLE
            userDetailsLayout.visibility = View.GONE
        }
        }

    private fun showUserDetails() {
        //val userData = getAllUserDataFromDatabase()
        val userDataList = dbHelper.getUserData()

        if (userDataList.isNotEmpty()) {
            val userDetailsText = buildString {
                for (userData in userDataList) {
                    append(
                        """
                Name: ${userData.name}
                Phone: ${userData.phone}
                Age: ${userData.age}
                Bio: ${userData.bio}
                Gender: ${userData.gender}
                Skills: ${userData.skills}
                
                
                """.trimIndent()
                    )
                }
            }
            userDetailsTextView.text = userDetailsText
        }

        else {

            userDetailsTextView.text = "No details found"
        }

        // Show the userDetailsLayout and hide the userDataLayout
        userDataLayout.visibility = View.GONE
        userDetailsLayout.visibility = View.VISIBLE


    }

    private fun saveUserData() {
        val name = nameEditText.text.toString()
        val phone = phoneEditText.text.toString()
        val age = ageEditText.text.toString().toIntOrNull() ?: 0
        val bio = bioEditText.text.toString()
        val gender = if (maleCheckBox.isChecked) "Male" else if (femaleCheckBox.isChecked) "Female" else ""
        val skills = mutableListOf<String>()

        if (skill1Box.isChecked) skills.add(getString(R.string.skill1))
        if (skill2Box.isChecked) skills.add(getString(R.string.skill2))
        if (skill3Box.isChecked) skills.add(getString(R.string.skill3))
        if (skill4Box.isChecked) skills.add(getString(R.string.skill4))

        val otherSkill = skillEditText.text.toString()
        if (otherSkill.isNotBlank()) {
            skills.add(otherSkill)
        }

        // Convert skills to JSON array
        val skillsArray = JSONArray(skills)

        // Store JSON array as a string in the database
        val skillsString = skillsArray.toString()

        val userData = UserData(name, phone, age, bio, gender, skillsString)
        val rowId = dbHelper.insertUserData(userData)

        if (rowId != -1L) {

            val successMessage = "User data inserted successfully. Row ID: $rowId"
            showToast(successMessage)
        } else {

            val errorMessage = "Failed to insert user data."
            showToast(errorMessage)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
    }

}




