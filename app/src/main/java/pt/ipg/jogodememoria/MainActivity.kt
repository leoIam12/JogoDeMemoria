package pt.ipg.jogodememoria

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.delay

import pt.ipg.jogodememoria.ui.theme.JogoDeMemoriaTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JogoDeMemoriaTheme {
                 Surface(
                     modifier = Modifier.fillMaxSize(),
                     color = MaterialTheme.colorScheme.background
                 ) {
                    TelaInicial(
                        modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize()
                    )

                }
            }
        }
    }
}


@Composable
fun MemoriaDoJogo(){
    val controladorNav = rememberNavController()
}

@Composable
fun TelaInicial(modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)

    ) {
        Column {
            Text(text = "Jogo de Memória",
                color = Color.Black,
                fontSize = 35.sp,
                modifier = Modifier.padding(bottom = 150.dp, start = 50.dp)
            )
            Image(painter = painterResource(id = R.drawable.todosdopocoyo), contentDescription = null)
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Iniciar Jogo")
            }
        }
    }
}

@Composable
fun TelaJogo(){
    var pontuacao by remember { mutableStateOf(0) }
    val cartoes = remember {
        val listaCartao = mutableListOf<CardData>()
        val cartaoAleatorio = Random(System.currentTimeMillis())
        
        val todoCartao = listOf(
            R.drawable.eli,
            R.drawable.pato,
            R.drawable.pocoyo,
            R.drawable.passaropai
        )
        
        val selecionarCartao = (0 until 4).flatMap { index ->
            val cartao = todoCartao[cartaoAleatorio.nextInt(0, todoCartao.size)]
            listOf(cartao, cartao)
        }
        selecionarCartao.shuffled().forEachIndexed { index, image ->
            listaCartao.add(CardData(index, image, R.drawable.imgcarta, index*10))
        }
        listaCartao
    }
    val cartaVirada = remember { mutableStateListOf<Int>() }
    val cartaCombinada = remember { mutableStateListOf<Int>() }
    var tempoRestante by remember { mutableStateOf(60) }

    LaunchedEffect(Unit) {
        //contagem regressiva do tempo
        while (tempoRestante > 0) {
            delay(100)
            tempoRestante--
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Text(text = "Score: $pontuacao",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
        )
        Text(text = "Tempo: $tempoRestante",
            fontSize = 24.sp,
            modifier = Modifier
                .padding(16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CardGrid(
    cartoes: List<CardData>,
    cartaVirada: List<Int>,
    cartaCombinada: List<Int>,
    cartaClicada: (Int) -> Unit
) {}

@Preview(showBackground = true)
@Composable
fun JogoDeMemoriaPreview() {
    JogoDeMemoriaTheme {
        TelaInicial(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize()
        )
    }
}

data class CardData(val id: Int, val image: Int, val backImage: Int, val value: Int)
