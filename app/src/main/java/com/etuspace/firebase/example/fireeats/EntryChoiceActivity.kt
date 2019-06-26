package com.etuspace.firebase.example.fireeats

import android.content.Intent
import com.etuspace.firebase.example.fireeats.java.MainActivity
import com.firebase.example.internal.BaseEntryChoiceActivity
import com.firebase.example.internal.Choice

class EntryChoiceActivity : BaseEntryChoiceActivity() {

    override fun getChoices(): List<Choice> {
        return listOf(
                Choice(
                        "Здравствуйте! Вас привествует приложение-ассистент для выполнения лабораторных работ по физике!",
                        "Для начала работы войдите в свой аккаунт",
                        Intent(this, MainActivity::class.java))

        )
    }
}
