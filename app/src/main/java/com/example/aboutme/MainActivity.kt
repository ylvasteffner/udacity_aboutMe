package com.example.aboutme

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.aboutme.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // The name ActivityMainBinding is also generated, based on the xml files name
    // (but we must remember to put a layout tag around all content and and move the xlmn:app stuff to it.
    // All this just magically works then because of the enabling of databinding in the gradle file.
    private lateinit var binding: ActivityMainBinding
    private val myName: MyName = MyName("Ylva S")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.myName = myName

        // This way of doing it is computing heavy and can slow down the app - so we swicth to binding!
/*        findViewById<Button>(R.id.done_button).setOnClickListener {buttonView ->
            addNickName(buttonView)
        }*/

        // Using binding instead generates a kind of middle layer that saves computing efforts.
        // The name doneButton is automatically generated for the button
        binding.doneButton.setOnClickListener { addNickName(it) }
    }

    fun addNickName(buttonView: View) {
        // Way of doing it with find views by id!
        // "findViewByID traverses the view hierarchy at runtime to find a view every time it's called" Not efficient!
/*        //1 Find views:
        var nickname_edit = findViewById<EditText>(R.id.nickname_edit)
        var nickname_textView = findViewById<TextView>(R.id.nickname_text)

        //2 Set text
        nickname_textView.text = nickname_edit.text

        //3 Update visibility
        nickname_edit.visibility = View.GONE
        nickname_textView.visibility = View.VISIBLE
        buttonView.visibility = View.GONE*/


        // Way of doing it with binding!
/*
        binding.nicknameText.text = binding.nicknameEdit.text
        binding.nicknameEdit.visibility = View.GONE
        binding.doneButton.visibility = View.GONE
        binding.nicknameText.visibility = View.VISIBLE
*/
        // Also a way of doing it using binding
        binding.apply {
            //nicknameText.text = nicknameEdit.text - using data binding
            myName?.nickname = nicknameEdit.text.toString() // - binding data to data binding - that was a weird sentence?
            // This was a strange thing? "In order to refresh the UI with the new data we need to
            // invalidate all bindings expressions so that they get recreated with the new values"
            // But why invalidate it just here, not above the text or below the views? Is it only text fields that needs to be regenerated?
            // Views are different then text is the short answer
            // And why invalidateAll() is needed at all seems to also depend on context, here it doesn't seem to actually be needed according to some udacity student comments?
            // This is comment from udacity mentor on their webpage:
            // If you do not want to call invalidateAll() you can use livedata with databinding to update the relevant ui views.
            // https://developer.android.com/topic/libraries/data-binding/architecture?authuser=1
            invalidateAll()
            nicknameEdit.visibility = View.GONE
            doneButton.visibility = View.GONE
            nicknameText.visibility = View.VISIBLE
        }
        //4 Hide the keyboard.
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(buttonView.windowToken, 0)
    }
}

// Learnings: databinding is a nice performance gain but the real power of data binding comes when we
// bind data. You can bind a view to a data class so that the data is directly available to the view

// The big idea about data binding is to create an object that connects/maps/binds two pieces of
// distant information together at compile time, so that you don't have to look for it at runtime.
// findViewById is a costly operation because it traverses the view hierarchy every time it is called.
// With data binding enabled, the compiler creates references to all views in a <layout> that have
// an id, and gathers them in a Binding object.


// Updating data and then updating the data displayed in views is cumbersome and a source of errors.
// Keeping the data in the view also violates separation of data and presentation.
// Data binding solves both of these problems. You keep data in a data class. You add a <data> block to
// the <layout> to identify the data as variables to use with the views. Views reference the variables.