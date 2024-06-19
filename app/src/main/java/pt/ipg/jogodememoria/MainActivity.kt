package pt.ipg.jogodememoria

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
                    MemoriaDoJogo(this)
                }
            }
        }
    }
}


@Composable
fun MemoriaDoJogo(activity: ComponentActivity){
    val controladorNav = rememberNavController()
    NavHost(navController = controladorNav, startDestination = "tela_main") {
        composable("tela_main") { TelaInicial(controladorNav)}
        composable("tela_jogo") { TelaJogo(controladorNav)}
        composable("tela_venceu") { TelaVenceu(controladorNav, activity) }
        composable("tela_perdeu") { TelaPerdeu(controladorNav, activity
        ) }
    }
}

@Composable
fun TelaInicial(controladorNav: NavHostController) {
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
            Button(onClick = { controladorNav.navigate("tela_jogo") },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Iniciar Jogo")
            }
        }
    }
}

@Composable
fun TelaJogo(controladorNav: NavHostController) {
    var pontuacao by remember { mutableStateOf(0) }
    var tempoRestante by remember { mutableStateOf(60) }
    val pontuacaoPorCombinacao = 15
    val numColunas = 4 // Definir o número de colunas
    val numeroCartas = 4 // Definir o número de pares de cartas
    val cartoes = remember {
        val listaCartao = mutableListOf<CardData>()
        val cartaoAleatorio = Random(System.currentTimeMillis())

        val todoCartao = listOf(
            R.drawable.eli,
            R.drawable.pato,
            R.drawable.pocoyo,
            R.drawable.passaropai
        ).take(numeroCartas)

        // Selecionar cartas aleatoriamente
        val selecionarCartao = todoCartao.flatMap { cartao ->
            listOf(cartao, cartao)
        }.shuffled(cartaoAleatorio)

        selecionarCartao.forEachIndexed { index, image ->
            listaCartao.add(CardData(index, image, R.drawable.ic_launcher_background))
        }
        listaCartao
    }
    val cartaVirada = remember { mutableStateListOf<Int>() }
    val cartaCombinada = remember { mutableStateListOf<Int>() }

    // Contador de tempo
    LaunchedEffect(Unit) {
        while (tempoRestante > 0) {
            delay(1000L) // Decrementa a cada segundo
            tempoRestante--
            if (cartaCombinada.size == cartoes.size) {
                controladorNav.navigate("tela_venceu")
                break
            }
        }
        if (tempoRestante == 0) {
            controladorNav.navigate("tela_perdeu")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f) // Ocupa a maior parte da tela
                .padding(16.dp)
        ) {
            GradeCarta(cartoes, cartaVirada, cartaCombinada, numColunas) { index ->
                if (cartaVirada.size < 2 && index !in cartaVirada && index !in cartaCombinada) {
                    cartaVirada.add(index)
                    if (cartaVirada.size == 2) {
                        val carta1 = cartoes[cartaVirada[0]]
                        val carta2 = cartoes[cartaVirada[1]]
                        if (carta1.image == carta2.image) {
                            cartaCombinada.addAll(cartaVirada)
                            pontuacao += pontuacaoPorCombinacao
                        }
                        cartaVirada.clear()
                    }
                }
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = "Pontuação: $pontuacao", fontSize = 20.sp, color = Color.Black)
            Text(text = "Tempo Restante: $tempoRestante", fontSize = 20.sp, color = Color.Black)
        }
    }
}

@Composable
fun GradeCarta(
    cartoes: List<CardData>,
    cartaVirada: List<Int>,
    cartaCombinada: List<Int>,
    numColunas: Int,
    cartaClicada: (Int) -> Unit
) {
    Column {
        for (i in cartoes.indices step numColunas) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                for (j in 0 until numColunas) {
                    if (i + j < cartoes.size) {
                        ItemCarta(cartoes[i + j], i + j in cartaVirada || i + j in cartaCombinada) {
                            cartaClicada(i + j)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemCarta(carta: CardData, aberta: Boolean, clicada: () -> Unit) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .padding(6.dp) // Adiciona espaço entre as cartas
            .clickable { clicada() }
    ) {
        Image(
            painter = painterResource(id = if (aberta) carta.image else carta.backImage),
            contentDescription = null
        )
    }
}

data class CardData(
    val id: Int,
    val image: Int,
    val backImage: Int
)

@Composable
fun TelaVenceu(controladorNav: NavHostController, activity: ComponentActivity) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Green),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Você Venceu!",
                color = Color.White,
                fontSize = 35.sp,
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { controladorNav.navigate("tela_jogo") }) {
                Text(text = "Jogar Novamente")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { activity.finish() }) {
                Text(text = "Sair do Jogo")
            }
        }
    }
}

@Composable
fun TelaPerdeu(controladorNav: NavHostController, activity: ComponentActivity) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Red),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Você Perdeu!",
                color = Color.White,
                fontSize = 35.sp,
                modifier = Modifier.padding(20.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(onClick = { controladorNav.navigate("tela_jogo") }) {
                Text(text = "Tentar Novamente")
            }
            Spacer(modifier = Modifier.height(20.dp))
            Button(onClick = { activity.finish() }) {
                Text(text = "Sair do Jogo")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TelaPerdeuPreview() {
    JogoDeMemoriaTheme {
        TelaPerdeu(rememberNavController(), activity = ComponentActivity())
    }
}

@Preview(showBackground = true)
@Composable
fun TelaVenceuPreview() {
    JogoDeMemoriaTheme {
        TelaVenceu(rememberNavController(), activity = ComponentActivity())
    }
}

@Preview(showBackground = true)
@Composable
fun TelaJogoPreview() {
    JogoDeMemoriaTheme {
        TelaJogo(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun TelaInicialPreview() {
    JogoDeMemoriaTheme {
        TelaInicial(rememberNavController())
    }
}
