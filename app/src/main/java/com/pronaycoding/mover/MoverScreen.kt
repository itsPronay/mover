package com.pronaycoding.mover

//import androidx.compose.foundation.layout.FlowRowScopeInstance.align
//import androidx.compose.foundation.layout.FlowRowScopeInstance.weight
//import androidx.compose.foundation.layout.ColumnScopeInstance.weight
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun MoverScreen(modifier: Modifier = Modifier) {
    var context = LocalContext.current

    var horizontalSpacer by rememberSaveable {
        mutableStateOf(1)
    }
    var verticalSpacer by rememberSaveable {
        mutableStateOf(1)
    }
    var buttonText by rememberSaveable {
        mutableStateOf("START")
    }
    var moverValue = 1
    var isRunning by remember { mutableStateOf(false) }

    var rightCLicked by rememberSaveable {
        mutableStateOf(true)
    }
    var leftClicked by rememberSaveable {
        mutableStateOf(false)
    }
    var topClicked by rememberSaveable {
        mutableStateOf(false)
    }
    var bottomClicked by rememberSaveable {
        mutableStateOf(false)
    }
    var score by rememberSaveable {
        mutableStateOf(0)
    }
    val scoreDistance = 20
    var scope = rememberCoroutineScope()

    /**
     * Screen size
     * Modify it by your needs
     */
    val screenWidthInDp = 350
    val screenHeightIntDp = 500

    /**
     * Random food/circle values
     */
    var horizontalValueCircle by rememberSaveable {
        mutableIntStateOf(Random.nextInt(0, screenWidthInDp))
    }
    var verticalValueCircle by rememberSaveable {
        mutableIntStateOf(Random.nextInt(0, screenHeightIntDp))
    }
    var eaten by rememberSaveable {
        mutableStateOf(false)
    }
    var gameOver by rememberSaveable {
        mutableStateOf(false)
    }

    if (gameOver) {
        AlertDialog(title = {
            Text(text = "Game over")
        }, text = {
            Text(text = "Would you like to play again?")
        }, icon = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_rocket_launch_24),
                contentDescription = ""
            )
        }, onDismissRequest = { }, confirmButton = {
            TextButton(onClick = {

                /**
                move right when the game starts again
                 */
                leftClicked = false
                rightCLicked = true
                bottomClicked = false
                topClicked = false

                horizontalSpacer = 1
                verticalSpacer = 1

                isRunning = true
                gameOver = false


            }) {
                Text(text = "Play again")
            }
        })
    }
    var horizontalDiff: Int
    var verticalDiff: Int

    LaunchedEffect(key1 = isRunning) {
        while (isRunning) {

            /**
             * food eating logic
             * when the difference is lower than scoreDistance, then the point increases
             */
            horizontalDiff = abs(horizontalSpacer - horizontalValueCircle)
            verticalDiff = abs(verticalSpacer - verticalValueCircle)
            if ((horizontalDiff <= scoreDistance) && verticalDiff <= scoreDistance) {
                eaten = true
                score += 1
            }

            /**
             * game over logic
             */
            if (horizontalSpacer == 0 || verticalSpacer == 0 || horizontalSpacer == screenWidthInDp || verticalSpacer == screenHeightIntDp) {
                isRunning = false
                gameOver = true
                score = 0

            }



            /**
             * We have to move the food when eaten = true
             */
            if (eaten) {
                var random = Random.nextInt(0, screenWidthInDp)
                var symbol = Random.nextInt(0, 1)

                /**
                 * considering 1 as + and 0 as -
                 */
                if (symbol == 1 && random + horizontalValueCircle <= screenWidthInDp) {
                    horizontalValueCircle += random
                } else if (horizontalValueCircle - random >= 0) {
                    horizontalValueCircle -= random
                } else if (horizontalValueCircle + random <= screenWidthInDp) {
                    horizontalValueCircle += random
                }

                symbol = Random.nextInt(0, 1)
                random = Random.nextInt(0, screenHeightIntDp)

                /**
                 * vertical position
                 */
                if (symbol == 1 && random + verticalValueCircle <= screenHeightIntDp) {
                    verticalValueCircle += random
                } else if (verticalValueCircle - random >= 0) {
                    verticalValueCircle -= random
                } else if (verticalValueCircle + random <= screenHeightIntDp) {
                    verticalValueCircle += random
                }
                eaten = false
            }

            /**
             * snake moving logic
             */
            delay(5L)

            if (rightCLicked && (horizontalSpacer + moverValue <= screenWidthInDp)) {
                horizontalSpacer += moverValue
            } else if (leftClicked && (horizontalSpacer - moverValue >= 0)) {
                horizontalSpacer -= moverValue
            } else if (topClicked && (verticalSpacer - moverValue >= 0)) {
                verticalSpacer -= moverValue
            } else if (bottomClicked && (verticalSpacer + moverValue <= screenHeightIntDp)) {
                verticalSpacer += moverValue
            }
        }
    }

    Scaffold(topBar = {
        TopAppBar(title = { Text(text = "Score: $score") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.outlineVariant
            ),
            actions = {
                var iconButtonLogo = R.drawable.baseline_play_arrow_24
                if (isRunning) {
                    iconButtonLogo = R.drawable.baseline_pause_24
                }
                IconButton(onClick = {
                    isRunning = !isRunning
                }) {
                    Icon(painter = painterResource(id = iconButtonLogo), contentDescription = null)
                }
            })
    }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
                .padding(5.dp)
        ) {
            Box(
                modifier = Modifier.weight(.8f)
            ) {
                Card(
                    modifier = Modifier
                        .size(20.dp)
                        .offset(
                            x = horizontalValueCircle.dp, y = verticalValueCircle.dp
                        ), shape = RoundedCornerShape(20.dp), colors = CardDefaults.cardColors(
                        containerColor = Color.Green
                    )
                ) {

                }
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Spacer(modifier = Modifier.height(verticalSpacer.dp))
                    Row(modifier = Modifier.fillMaxSize()) {
                        Text(text = "")
                        Spacer(modifier = Modifier.width(horizontalSpacer.dp))
                        IconButton(modifier = Modifier.size(40.dp),
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.inverseOnSurface
                            ),
                            onClick = {
                                Toast.makeText(
                                    context, "Don't hurt my penguin please :( ", Toast.LENGTH_SHORT
                                ).show()
                            }) {
                            Icon(
                                painter = painterResource(id = R.drawable.penguin),
                                contentDescription = "",
                                tint = Color.Unspecified
                            )
                        }
                    }
                }
                Column {
                    Spacer(modifier = Modifier.height((screenHeightIntDp + 40).dp))
                    Divider()

                }

            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(.2f)
                    .background(MaterialTheme.colorScheme.inverseOnSurface)
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconButton(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                        onClick = {
                            scope.launch(Dispatchers.Default) {
                                leftClicked = false
                                rightCLicked = false
                                bottomClicked = false
                                topClicked = true
                            }
                        }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowUp, contentDescription = "")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(onClick = {
                            scope.launch(Dispatchers.Default) {
                                leftClicked = true
                                rightCLicked = false
                                bottomClicked = false
                                topClicked = false
                            }

                        }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                        }

                        Spacer(modifier = Modifier.width(100.dp))
                        IconButton(onClick = {
                            scope.launch(Dispatchers.Default) {
                                leftClicked = false
                                rightCLicked = true
                                bottomClicked = false
                                topClicked = false
                            }

                        }) {
                            Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "")
                        }
                    }
                    IconButton(modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.CenterHorizontally),
                        onClick = {
                            scope.launch(Dispatchers.Default) {
                                leftClicked = false
                                rightCLicked = false
                                bottomClicked = true
                                topClicked = false
                            }

                        }) {
                        Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "")
                    }

                }
            }
        }
    }
}