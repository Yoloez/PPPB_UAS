import com.example.pppb_uas.model.Course


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pppb_uas.ui.Dosen.AddDosenScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    onBackClick: () -> Unit = {},
    onSaveClick: (Course) -> Unit = {}
) {
    var courseData by remember { mutableStateOf(Course()) }

    val darkGreen = Color(0xFF015023)
    val lightGreen = Color(0xFF015023)
    val yellowButton = Color(0xFFDABC4E)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Add Course",
                        color = Color.White,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF015023)
                )
            )
        },
        containerColor = Color(0xFF015023)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))



            // Course Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Course",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = courseData.course,
                    onValueChange = { courseData = courseData.copy(course = it) },
                    modifier = Modifier
                        .fillMaxWidth(),

                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,

                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ID Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "ID:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = courseData.id,
                    onValueChange = { courseData = courseData.copy(id = it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(darkGreen),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,

                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Semester Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Semester:",
                    color = Color.White,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = courseData.semester,
                    onValueChange = { courseData = courseData.copy(semester = it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(darkGreen),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = lightGreen,
                        unfocusedContainerColor = lightGreen,
                        focusedBorderColor = Color.Black,
                        unfocusedBorderColor = Color.Black,

                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
            }

            Spacer(modifier = Modifier.height(50.dp))
            // Save Button
            Button(
                onClick = { onSaveClick(courseData) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),

                colors = ButtonDefaults.buttonColors(
                    containerColor = yellowButton
                ),
                shape = RoundedCornerShape(28.dp)
            )
          {
                Text(
                    text = "Save",
                    color = darkGreen,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun AddCourseScreenPreview() {
    AddCourseScreen()
}