package com.raitha.bharosa.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.raitha.bharosa.data.models.AppLanguage
import com.raitha.bharosa.ui.theme.FarmGreen
import com.raitha.bharosa.ui.theme.SkyBlue
import com.raitha.bharosa.data.models.ChatMessage

@Composable
fun ChatScreen(
    messages: List<ChatMessage>,
    isLoading: Boolean,
    selectedLanguage: AppLanguage,
    onSendMessage: (String) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit
) {

    var inputText by remember {
        mutableStateOf("")
    }

    val listState = rememberLazyListState()

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    val quickPrompts = getQuickPrompts(selectedLanguage)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        // HEADER

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(SkyBlue)
                .padding(16.dp)
        ) {

            Column {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            text = getLangText(
                                selectedLanguage,
                                "AI Farming Expert",
                                "AI ಕೃಷಿ ತಜ್ಞ",
                                "AI कृषि विशेषज्ञ",
                                "AI వ్యవసాయ నిపుణుడు"
                            ),
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = getLangText(
                                selectedLanguage,
                                "Powered by Gemini AI",
                                "Gemini AI ನಿಂದ ಚಾಲಿತ",
                                "Gemini AI द्वारा संचालित",
                                "Gemini AI ద్వారా నడుపబడుతుంది"
                            ),
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }

                    // LANGUAGE MENU

                    var showLangMenu by remember {
                        mutableStateOf(false)
                    }

                    Box {

                        OutlinedButton(
                            onClick = {
                                showLangMenu = true
                            },
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color.White
                            )
                        ) {

                            Text(
                                text = selectedLanguage.nativeName,
                                fontSize = 12.sp
                            )

                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                        }

                        DropdownMenu(
                            expanded = showLangMenu,
                            onDismissRequest = {
                                showLangMenu = false
                            }
                        ) {

                            AppLanguage.values().forEach { lang ->

                                DropdownMenuItem(
                                    text = {
                                        Text("${lang.nativeName} (${lang.displayName})")
                                    },
                                    onClick = {
                                        onLanguageChange(lang)
                                        showLangMenu = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // QUICK PROMPTS

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {

                    items(quickPrompts) { prompt ->

                        AssistChip(
                            onClick = {
                                onSendMessage(prompt.second)
                            },

                            label = {
                                Text(
                                    text = prompt.first,
                                    fontSize = 11.sp
                                )
                            },

                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = Color.White.copy(alpha = 0.2f),
                                labelColor = Color.White
                            )
                        )
                    }
                }
            }
        }

        // MESSAGES

        LazyColumn(
            modifier = Modifier.weight(1f),
            state = listState,
            contentPadding = PaddingValues(
                horizontal = 16.dp,
                vertical = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(messages) { message ->
                ChatBubble(message)
            }

            if (isLoading) {

                item {

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        TypingIndicator()
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        // INPUT AREA

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = MaterialTheme.colorScheme.surface
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    ),
                verticalAlignment = Alignment.Bottom
            ) {

                OutlinedTextField(
                    value = inputText,

                    onValueChange = {
                        inputText = it
                    },

                    modifier = Modifier.weight(1f),

                    placeholder = {

                        Text(
                            text = getLangText(
                                selectedLanguage,
                                "Ask about crops, diseases, weather...",
                                "ಬೆಳೆ, ರೋಗ, ಹವಾಮಾನ ಕೇಳಿ...",
                                "फसल, रोग, मौसम के बारे में पूछें...",
                                "పంటలు, వ్యాధులు, వాతావరణం గురించి అడగండి..."
                            ),
                            fontSize = 13.sp
                        )
                    },

                    shape = RoundedCornerShape(24.dp),

                    maxLines = 3,

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SkyBlue,
                        unfocusedBorderColor = Color.Gray.copy(alpha = 0.4f)
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                FloatingActionButton(

                    onClick = {

                        if (inputText.isNotBlank() && !isLoading) {

                            onSendMessage(inputText.trim())

                            inputText = ""
                        }
                    },

                    containerColor =
                        if (inputText.isNotBlank())
                            SkyBlue
                        else
                            Color.Gray,

                    contentColor = Color.White,

                    modifier = Modifier.size(48.dp)

                ) {

                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Send"
                    )
                }
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {

    Row(
        modifier = Modifier.fillMaxWidth(),

        horizontalArrangement =
            if (message.isUser)
                Arrangement.End
            else
                Arrangement.Start
    ) {

        if (!message.isUser) {

            Surface(
                modifier = Modifier.size(34.dp),
                shape = CircleShape,
                color = SkyBlue
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "🤖",
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(

            shape = RoundedCornerShape(
                topStart = if (message.isUser) 20.dp else 4.dp,
                topEnd = if (message.isUser) 4.dp else 20.dp,
                bottomStart = 20.dp,
                bottomEnd = 20.dp
            ),

            color =
                if (message.isUser)
                    FarmGreen
                else
                    MaterialTheme.colorScheme.surface,

            shadowElevation = 2.dp,

            modifier = Modifier.widthIn(max = 280.dp)

        ) {

            Text(
                text = message.content,

                modifier = Modifier.padding(12.dp),

                color =
                    if (message.isUser)
                        Color.White
                    else
                        MaterialTheme.colorScheme.onSurface,

                fontSize = 14.sp,

                lineHeight = 20.sp
            )
        }

        if (message.isUser) {

            Spacer(modifier = Modifier.width(8.dp))

            Surface(
                modifier = Modifier.size(34.dp),
                shape = CircleShape,
                color = FarmGreen.copy(alpha = 0.2f)
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = "👨‍🌾",
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TypingIndicator() {

    Surface(
        shape = RoundedCornerShape(
            topStart = 4.dp,
            topEnd = 20.dp,
            bottomStart = 20.dp,
            bottomEnd = 20.dp
        ),

        color = MaterialTheme.colorScheme.surface,

        shadowElevation = 2.dp
    ) {

        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 12.dp
            ),

            horizontalArrangement = Arrangement.spacedBy(4.dp),

            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "Thinking",
                color = Color.Gray,
                fontSize = 13.sp
            )

            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                strokeWidth = 2.dp,
                color = SkyBlue
            )
        }
    }
}

fun getQuickPrompts(
    language: AppLanguage
): List<Pair<String, String>> {

    return when (language) {

        AppLanguage.KANNADA -> listOf(
            "🌧️ ಮಳೆ ಬೆಳೆ" to "ಮಳೆ ಅವಲಂಬಿತ ಬೆಳೆಗಳ ಬಗ್ಗೆ ಹೇಳಿ",
            "🐛 ರೋಗ" to "ರಾಗಿ ರೋಗ ಮತ್ತು ಚಿಕಿತ್ಸೆ",
            "💊 ಕ್ರಿಮಿನಾಶಕ" to "ಸಾವಯವ ಕ್ರಿಮಿನಾಶಕ ಮಾಹಿತಿ",
            "🌱 ಸರ್ಕಾರಿ" to "ರೈತರ ಸರ್ಕಾರಿ ಯೋಜನೆಗಳು"
        )

        AppLanguage.HINDI -> listOf(
            "🌧️ वर्षा" to "वर्षा आधारित फसलों के बारे में बताएं",
            "🐛 रोग" to "गेहूं के रोग और उपचार",
            "💊 कीटनाशक" to "जैविक कीटनाशक जानकारी",
            "🌱 सरकारी" to "किसान सरकारी योजनाएं"
        )

        AppLanguage.TELUGU -> listOf(
            "🌧️ వర్షం" to "వర్షాధార పంటల గురించి చెప్పండి",
            "🐛 వ్యాధి" to "రాగి వ్యాధులు మరియు చికిత్స",
            "💊 పురుగుమందు" to "జైవిక పురుగుమందు సమాచారం",
            "🌱 ప్రభుత్వం" to "రైతు ప్రభుత్వ పథకాలు"
        )

        else -> listOf(
            "🌧️ Rain crops" to "Tell me about rain-fed crops in Karnataka",
            "🐛 Crop disease" to "Ragi disease symptoms and treatment",
            "💊 Pesticides" to "Organic pesticide information",
            "🌱 Schemes" to "Government schemes for farmers"
        )
    }
}