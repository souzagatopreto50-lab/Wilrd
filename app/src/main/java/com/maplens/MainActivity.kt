package com.maplens

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ImageSearch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MapLensApp() }
    }
}

@Composable
private fun MapLensApp() {
    val context = LocalContext.current
    var selectedUri by remember { mutableStateOf<Uri?>(null) }
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            runCatching {
                context.contentResolver.takePersistableUriPermission(
                    uri, android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            }
            selectedUri = uri
        }
    }

    val bitmap = remember(selectedUri) {
        selectedUri?.let { uri ->
            runCatching {
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it)
                }
            }.getOrNull()
        }
    }

    MaterialTheme(colorScheme = darkColorScheme(
        primary = Color(0xFF8BE9C2),
        background = Color(0xFF0B1118),
        surface = Color(0xFF151F2A)
    )) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color(0xFF0B1118)
        ) {
            Column(
                modifier = Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier.size(46.dp).background(
                            Color(0xFF183B35), RoundedCornerShape(14.dp)
                        ),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Icon(
                            Icons.Default.ImageSearch, null, tint = Color(0xFF8BE9C2)
                        )
                    }
                    Column(Modifier.padding(start = 12.dp)) {
                        Text("MAPLENS", fontSize = 22.sp, fontWeight = FontWeight.Black,
                            color = Color.White)
                        Text("Análise de capturas", color = Color(0xFF9BAAB8), fontSize = 13.sp)
                    }
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF151F2A)),
                    shape = RoundedCornerShape(22.dp),
                    modifier = Modifier.fillMaxWidth().weight(1f)
                ) {
                    if (bitmap != null) {
                        Column(Modifier.fillMaxSize().padding(12.dp)) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Imagem selecionada para análise",
                                modifier = Modifier.fillMaxWidth().weight(1f),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "Imagem carregada • ${bitmap.width} × ${bitmap.height}",
                                color = Color(0xFF8BE9C2), fontSize = 13.sp
                            )
                        }
                    } else {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            androidx.compose.material3.Icon(
                                Icons.Default.AddPhotoAlternate, null,
                                modifier = Modifier.size(58.dp),
                                tint = Color(0xFF8BE9C2)
                            )
                            Spacer(Modifier.height(16.dp))
                            Text("Sua captura começa aqui", color = Color.White,
                                fontSize = 19.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(8.dp))
                            Text(
                                "Importe uma imagem salva no aparelho para visualizar e revisar.",
                                color = Color(0xFF9BAAB8), fontSize = 14.sp
                            )
                        }
                    }
                }

                Button(
                    onClick = { picker.launch(arrayOf("image/*")) },
                    modifier = Modifier.fillMaxWidth().height(54.dp)
                ) {
                    androidx.compose.material3.Icon(Icons.Default.AddPhotoAlternate, null)
                    Text("  Escolher imagem")
                }
                OutlinedButton(
                    onClick = { selectedUri = null },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    enabled = selectedUri != null
                ) { Text("Limpar captura") }

                Text(
                    "MapLens trabalha com imagens escolhidas por você. Não lê a memória do jogo, " +
                        "não automatiza ações e não interfere em partidas.",
                    color = Color(0xFF748493), fontSize = 11.sp
                )
            }
        }
    }
}
