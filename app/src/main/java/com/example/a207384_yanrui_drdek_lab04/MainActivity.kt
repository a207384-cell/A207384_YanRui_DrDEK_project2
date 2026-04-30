package com.example.a207384_yanrui_drdek_lab04

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.a207384_yanrui_drdek_lab04.ui.theme.A207384_YanRui_DrDEK_Lab04Theme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            A207384_YanRui_DrDEK_Lab04Theme {
                HealthApp()
            }
        }
    }
}

// --- Navigation 路由 (满分要求: 5 个屏幕) ---
enum class HealthScreen { Dashboard, Entry, Summary, Profile, BmiCalc }

// --- 主控 App ---
@Composable
fun HealthApp(viewModel: HealthViewModel = viewModel()) {
    val navController = rememberNavController()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = HealthScreen.Dashboard.name,
            modifier = Modifier
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // 屏幕 1: Dashboard
            composable(route = HealthScreen.Dashboard.name) {
                DashboardScreen(
                    uiState = uiState,
                    onNavigateToLog = { navController.navigate(HealthScreen.Entry.name) },
                    onNavigateToProfile = { navController.navigate(HealthScreen.Profile.name) },
                    onNavigateToBmi = { navController.navigate(HealthScreen.BmiCalc.name) }
                )
            }
            // 屏幕 2: Entry (Add Item)
            composable(route = HealthScreen.Entry.name) {
                WaterEntryScreen(
                    uiState = uiState,
                    onWaterInputChanged = { viewModel.updateWaterInput(it) },
                    onCancelClicked = { navController.popBackStack() },
                    onSubmitClicked = {
                        viewModel.addWater()
                        navController.navigate(HealthScreen.Summary.name)
                    }
                )
            }
            // 屏幕 3: Summary (Item Detail/Feedback)
            composable(route = HealthScreen.Summary.name) {
                SummaryScreen(
                    uiState = uiState,
                    onReturnHomeClicked = {
                        viewModel.resetStatus()
                        navController.popBackStack(HealthScreen.Dashboard.name, inclusive = false)
                    }
                )
            }
            // 屏幕 4: Profile (Modify Item)
            composable(route = HealthScreen.Profile.name) {
                ProfileScreen(
                    uiState = uiState,
                    onNameChange = { viewModel.updateProfileNameInput(it) },
                    onGoalChange = { viewModel.updateProfileGoalInput(it) },
                    onSaveClick = {
                        viewModel.saveProfile()
                        navController.popBackStack()
                    },
                    onBackClick = { navController.popBackStack() }
                )
            }
            // 屏幕 5: BMI Calculator (Calculation/Processing)
            composable(route = HealthScreen.BmiCalc.name) {
                BmiCalcScreen(
                    uiState = uiState,
                    onWeightChange = { viewModel.updateWeightInput(it) },
                    onHeightChange = { viewModel.updateHeightInput(it) },
                    onCalculateClick = { viewModel.calculateBmi() },
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

// ================= UI 屏幕组件 =================

// --- 屏幕 1: 仪表盘 ---
@Composable
fun DashboardScreen(
    uiState: HealthUiState,
    onNavigateToLog: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToBmi: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Health Tracker", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            IconButton(onClick = onNavigateToProfile) {
                Icon(Icons.Filled.AccountCircle, contentDescription = "Profile", modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.primary)
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Box(modifier = Modifier
                .fillMaxSize()
                .padding(20.dp), contentAlignment = Alignment.CenterStart) {
                Column {
                    Text("Good morning, ${uiState.userName}!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                    Text("Let's track your health today", fontSize = 16.sp, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f))
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            StatCard("Calories", "1,850", "kcal", Modifier.weight(1f))
            StatCard("Steps", "8,432", "steps", Modifier.weight(1f))
            StatCard("Water", String.format(Locale.getDefault(), "%.1f / %.1f", uiState.totalWaterLiters, uiState.dailyWaterGoalLiters), "L", Modifier.weight(1f))
        }

        Text("Today's Focus", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
        SuggestionItem(Icons.Filled.Favorite, "Healthy Breakfast", "Oatmeal with fruits")
        SuggestionItem(Icons.Filled.PlayArrow, "Evening Walk", "30 minutes • 300 kcal")

        if (uiState.waterLogs.isNotEmpty()) {
            Text("Recent Water Logs", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.primary)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    uiState.waterLogs.forEachIndexed { index, amount ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Log #${uiState.waterLogs.size - index}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text("+$amount ml", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        if (index < uiState.waterLogs.lastIndex) {
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNavigateToLog, modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)) {
            Text("Log Water Intake")
        }

        OutlinedButton(onClick = onNavigateToBmi, modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)) {
            Text("Calculate BMI")
        }

        Text("Matric No: A207384", style = MaterialTheme.typography.bodySmall, color = Color.Gray, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Spacer(modifier = Modifier.height(24.dp))
    }
}

// --- 屏幕 2: 输入页 ---
@Composable
fun WaterEntryScreen(
    uiState: HealthUiState,
    onWaterInputChanged: (String) -> Unit,
    onSubmitClicked: () -> Unit,
    onCancelClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Log Daily Water", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 24.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                OutlinedTextField(
                    value = uiState.waterInput,
                    onValueChange = onWaterInputChanged,
                    label = { Text("Enter today's water intake (ml)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(onClick = onCancelClicked, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(onClick = onSubmitClicked, modifier = Modifier.weight(1f)) { Text("Save") }
                }
            }
        }
    }
}

// --- 屏幕 3: 总结页 ---
@Composable
fun SummaryScreen(uiState: HealthUiState, onReturnHomeClicked: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = uiState.resultMessage,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    textAlign = TextAlign.Center
                )
                Button(onClick = onReturnHomeClicked, modifier = Modifier.fillMaxWidth()) {
                    Text("Return to Dashboard")
                }
            }
        }
    }
}

// --- 屏幕 4: 用户资料页 (修改数据演示) ---
@Composable
fun ProfileScreen(
    uiState: HealthUiState,
    onNameChange: (String) -> Unit,
    onGoalChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Edit Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 24.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(8.dp)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedTextField(
                    value = uiState.profileNameInput,
                    onValueChange = onNameChange,
                    label = { Text("Your Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.profileGoalInput,
                    onValueChange = onGoalChange,
                    label = { Text("Daily Water Goal (Liters)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    OutlinedButton(onClick = onBackClick, modifier = Modifier.weight(1f)) { Text("Cancel") }
                    Button(onClick = onSaveClick, modifier = Modifier.weight(1f)) { Text("Save Changes") }
                }
            }
        }
    }
}

// --- 屏幕 5: BMI 计算页 ---
@Composable
fun BmiCalcScreen(
    uiState: HealthUiState,
    onWeightChange: (String) -> Unit,
    onHeightChange: (String) -> Unit,
    onCalculateClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("BMI Calculator", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(bottom = 24.dp))
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), elevation = CardDefaults.cardElevation(8.dp)) {
            Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(
                    value = uiState.weightInput,
                    onValueChange = onWeightChange,
                    label = { Text("Weight (kg)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = uiState.heightInput,
                    onValueChange = onHeightChange,
                    label = { Text("Height (cm)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = onCalculateClick, modifier = Modifier.fillMaxWidth()) { Text("Calculate") }

                if (uiState.bmiResult > 0) {
                    Text(
                        "Your BMI: ${String.format(Locale.getDefault(), "%.1f", uiState.bmiResult)}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                OutlinedButton(onClick = onBackClick, modifier = Modifier.fillMaxWidth()) { Text("Back to Dashboard") }
            }
        }
    }
}

// ================= UI 小组件 =================
@Composable
fun StatCard(title: String, value: String, unit: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = title, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            Text(text = unit, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun SuggestionItem(imageVector: ImageVector, title: String, desc: String) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = imageVector, contentDescription = title, modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.secondary)
            Column(modifier = Modifier
                .padding(start = 12.dp)
                .weight(1f)) {
                Text(text = title, fontWeight = FontWeight.Medium)
                Text(text = desc, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}