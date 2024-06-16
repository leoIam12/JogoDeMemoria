package pt.ipg.jogodememoria

import android.os.Bundle
import android.view.Surface
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import pt.ipg.jogodememoria.ui.theme.JogoDeMemoriaTheme

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