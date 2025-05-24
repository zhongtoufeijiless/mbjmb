package com.tcssj.mbjmb.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tcssj.mbjmb.model.VerifyCodeBody
import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.ui.platform.LocalContext
import android.widget.Toast
import com.google.gson.GsonBuilder
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@Composable
fun VerifyCodeScreen(viewModel: VerifyCodeViewModel = viewModel()) {
    val state by viewModel.state.collectAsState()
    var mobile by remember { mutableStateOf(TextFieldValue("81991415335")) }
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "验证码接口 Demo",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
            ),
            modifier = Modifier.padding(top = 18.dp, bottom = 18.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp),
            elevation = CardDefaults.cardElevation(6.dp)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { mobile = it },
                    label = { Text("手机号") },
                    singleLine = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(14.dp))
                Button(
                    onClick = {
                        viewModel.handleIntent(VerifyCodeIntent.SendCode(mobile.text))
                    },
                    enabled = !state.loading,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("请求接口并显示结果", fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(14.dp))
                if (state.loading) {
                    CircularProgressIndicator()
                }
                state.result?.let { resp ->
                    ResultCard(resp.code, resp.msg, resp.data)
                }
                state.error?.let {
                    Text(
                        text = "错误: $it",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ResultCard(code: String?, msg: String?, data: VerifyCodeBody?) {
    val context = LocalContext.current
    val gson = remember { GsonBuilder().setPrettyPrinting().create() }
    val json = remember(data, code, msg) {
        gson.toJson(mapOf(
            "code" to code,
            "msg" to msg,
            "data" to data
        ))
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text("接口返回：", fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("状态码:", fontSize = 11.sp, color = Color(0xFF666666), modifier = Modifier.width(48.dp))
                    Text(code ?: "-", fontSize = 11.sp, color = Color(0xFF222222))
                }
                Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Text("消息:", fontSize = 11.sp, color = Color(0xFF666666), modifier = Modifier.width(48.dp))
                    Text(msg ?: "-", fontSize = 11.sp, color = Color(0xFF222222))
                }
                if (data != null) {
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("有效期:", fontSize = 11.sp, color = Color(0xFF666666), modifier = Modifier.width(48.dp))
                        Text("${data.expiry ?: "-"} 秒", fontSize = 11.sp, color = Color(0xFF222222))
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("状态:", fontSize = 11.sp, color = Color(0xFF666666), modifier = Modifier.width(48.dp))
                        Text(data.status ?: "-", fontSize = 11.sp, color = Color(0xFF222222))
                    }
                    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Text("流水号:", fontSize = 11.sp, color = Color(0xFF666666), modifier = Modifier.width(48.dp))
                        Text(data.transId ?: "-", fontSize = 11.sp, color = Color(0xFF222222))
                    }
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Divider(color = Color(0xFFE0E0E0), thickness = 1.dp)
            Spacer(modifier = Modifier.height(4.dp))
            Text("原始JSON：", fontWeight = FontWeight.Medium, fontSize = 11.sp, color = Color(0xFF4285F4))
            Spacer(modifier = Modifier.height(1.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp, max = 260.dp)
                .verticalScroll(rememberScrollState())
                .padding(1.dp)
            ) {
                SelectionContainer {
                    Box(Modifier.horizontalScroll(rememberScrollState())) {
                        Text(
                            text = json,
                            fontSize = 9.sp,
                            color = Color(0xFF333333),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(top = 1.dp)) {
                Icon(
                    imageVector = Icons.Filled.ContentCopy,
                    contentDescription = "复制JSON",
                    modifier = Modifier
                        .size(15.dp)
                        .clickable {
                            val clipboard = android.content.Context.CLIPBOARD_SERVICE
                            val clip = android.content.ClipData.newPlainText("json", json)
                            (context.getSystemService(clipboard) as android.content.ClipboardManager).setPrimaryClip(clip)
                            Toast.makeText(context, "已复制JSON", Toast.LENGTH_SHORT).show()
                        }
                )
                Spacer(modifier = Modifier.width(1.dp))
                Text("复制JSON", fontSize = 9.sp, color = Color(0xFF4285F4))
            }
        }
    }
} 