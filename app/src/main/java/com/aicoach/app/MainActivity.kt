package com.aicoach.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class CoachMessage(val text: String, val fromUser: Boolean)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { CoachApp() }
    }
}

@Composable
fun CoachApp() {
    var tab by remember { mutableStateOf("Home") }
    var input by remember { mutableStateOf("") }
    var messages by remember {
        mutableStateOf(listOf(CoachMessage(
            "Hi. I'm your AI coach. Tell me how you feel today, or ask what you should train.",
            false
        )))
    }

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(title = { Text("AI Coach") })
            },
            bottomBar = {
                NavigationBar {
                    listOf("Home", "Coach", "Training", "Nutrition", "Health", "Profile").forEach {
                        NavigationBarItem(
                            selected = tab == it,
                            onClick = { tab = it },
                            icon = {},
                            label = { Text(it) }
                        )
                    }
                }
            }
        ) { padding ->
            Box(Modifier.padding(padding).fillMaxSize()) {
                when (tab) {
                    "Home" -> HomeScreen(onCoach = { tab = "Coach" })
                    "Coach" -> CoachScreen(
                        messages = messages,
                        input = input,
                        onInput = { input = it },
                        onSend = {
                            val clean = input.trim()
                            if (clean.isNotEmpty()) {
                                messages = messages + CoachMessage(clean, true) +
                                    CoachMessage(
                                        "I’ve received that. In the production version I’ll combine your goals, training history, recovery and permitted health data before making a recommendation.",
                                        false
                                    )
                                input = ""
                            }
                        }
                    )
                    "Training" -> TrainingScreen()
                    "Nutrition" -> SimpleScreen("Nutrition", "Meal-photo analysis and portion estimation will connect here.")
                    "Health" -> SimpleScreen("Health", "Health Connect and Xiaomi Smart Band 9 Pro data will connect here.")
                    "Profile" -> SimpleScreen("Profile", "Your profile, goals, coaching style and privacy controls will live here.")
                }
            }
        }
    }
}

@Composable
fun HomeScreen(onCoach: () -> Unit) {
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("Good morning.", style = MaterialTheme.typography.headlineMedium)
        Text("Your coach combines your goals, training and recovery context.")
        Card {
            Column(Modifier.padding(18.dp)) {
                Text("Today's recommendation", style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(8.dp))
                Text("Demo: keep today's upper-body session moderate and avoid extra volume unless your warm-up feels normal.")
                Spacer(Modifier.height(12.dp))
                Button(onClick = onCoach) { Text("Talk to Coach") }
            }
        }
    }
}

@Composable
fun CoachScreen(
    messages: List<CoachMessage>,
    input: String,
    onInput: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(Modifier.fillMaxSize().padding(16.dp)) {
        LazyColumn(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(messages) { msg ->
                Surface(
                    color = if (msg.fromUser)
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        msg.text,
                        modifier = Modifier.padding(12.dp),
                        color = if (msg.fromUser)
                            MaterialTheme.colorScheme.onPrimary
                        else
                            MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = input,
                onValueChange = onInput,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Talk to your coach…") }
            )
            Button(onClick = onSend) { Text("Send") }
        }
    }
}

@Composable
fun TrainingScreen() {
    val exercises = listOf(
        "Incline Dumbbell Press — 3 × 8–12 — RPE 7–8",
        "Lat Pulldown — 3 × 8–12 — RPE 7–8",
        "Seated Cable Row — 3 × 10–12 — RPE 7–8",
        "Lateral Raise — 2 × 12–15 — RPE 7–8"
    )
    Column(Modifier.padding(20.dp)) {
        Text("Today's Workout", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        exercises.forEach { item ->
            Card(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
                Text(item, Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun SimpleScreen(title: String, description: String) {
    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        Text(description)
    }
}
