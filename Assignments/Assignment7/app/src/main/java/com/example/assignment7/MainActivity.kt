package com.example.assignment7
// Core Android imports
// import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// Compose UI imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ViewModel imports
import androidx.lifecycle.viewmodel.compose.viewModel

// TODO: Import MainViewModel from the same package once created
// import com.example.bookexamplesapp.MainViewModel
import com.example.assignment7.MainViewModel

/**
 * MainActivity is the entry point of the application.
 * It sets up the basic Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the Compose UI
        setContent {
            MaterialTheme {
                Surface {
                    // TODO: Call the StudentGradeManager composable here
                    StudentGradeManager()
                }
            }
        }
    }
}

// TODO: Define the Student data class
// It should have 'name' (String) and 'grade' (Int) properties.
data class Student(val name: String, val grade: Float)

@Composable
fun StudentGradeManager(
    // TODO: Instantiate MainViewModel using viewModel()
     viewModel: MainViewModel = viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp)
    ) {
        // Header
        // TODO: Add a Text composable for the header "Student Grade Manager"
        // Wrap this composable in an 'item {}' block.
        // Use style MaterialTheme.typography.headlineMedium and Modifier.padding(bottom = 16.dp)
        item{
            Text(text = "Student Grade Manager",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
                )
        }

        // TODO: Call GPADisplay composable, passing the GPA from the ViewModel
        // Wrap this composable in an 'item {}' block.
        item{
            val gpa =viewModel.calculateGPA()
            GPADisplay(gpa = gpa)
        }

        // TODO: Call AddStudentForm composable, passing state and event handlers from the ViewModel
        // Wrap this composable in an 'item {}' block.
        item{
            AddStudentForm(
                name = viewModel.newStudentName,
                grade = viewModel.newStudentGrade,
                onNameChange = viewModel :: updateNewStudentName,
                onGradeChange = viewModel :: updateNewStudentGrade,
                onAddStudent = {
                    viewModel.addStudent(
                        viewModel.newStudentName,
                        viewModel.newStudentGrade
                    )
                }
            )
        }


        // TODO: Create a Button to "Load Sample Data" that calls viewModel.loadSampleData()
        // Wrap this composable in an 'item {}' block.
        // Use Modifier.padding(vertical = 8.dp)
        item {

            Button(
                onClick = {viewModel.loadSampleData()},
                Modifier.padding(vertical = 8.dp)
            ) {Text("Load Sample Data") }

        }

        // TODO: Call StudentsList composable, passing the list of students and the remove student handler from the ViewModel
        // Wrap this composable in an 'item {}' block.
        item {
        StudentsList(
            students = viewModel.students,
            onRemoveStudent = viewModel::removeStudent
        )

        }

        // TODO: Conditionally show a CircularProgressIndicator if viewModel.isLoading is true
        // Wrap this composable in an 'item {}' block.
        // Use Modifier.padding(16.dp)
        item{
            if(viewModel.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )

            }
        }
    }
}

@Composable
fun GPADisplay(gpa: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TODO: Add a Text composable for "Class GPA"
            // Use style MaterialTheme.typography.titleMedium
            Text(
                text = "Class Gpa",
                style = MaterialTheme.typography.titleMedium
            )

            // TODO: Add a Text composable to display the formatted GPA (e.g., "%.2f".format(gpa))
            // Use style MaterialTheme.typography.headlineLarge and color MaterialTheme.colorScheme.primary
            Text(
                text = "%.2f".format(gpa),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun AddStudentForm(
    name: String,
    grade: String,
    onNameChange: (String) -> Unit,
    onGradeChange: (String) -> Unit,
    onAddStudent: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // TODO: Add a Text composable for "Add New Student"
            // Use style MaterialTheme.typography.titleMedium and Modifier.padding(bottom = 8.dp)
            Text(
                text = "Add New Student",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // TODO: Create an OutlinedTextField for student name input
            // Bind value to 'name', onValueChange to 'onNameChange', and set label to "Student Name"
            // Use Modifier.fillMaxWidth()
            OutlinedTextField(
                value = name,
                onValueChange = onNameChange,
                label = { Text("Student Name")},
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: Create an OutlinedTextField for student grade input
            // Bind value to 'grade', onValueChange to 'onGradeChange', and set label to "Grade (0-100)"
            // Set keyboardOptions to KeyboardOptions(keyboardType = KeyboardType.Number)
            // Use Modifier.fillMaxWidth()
            OutlinedTextField(
                value = grade,
                onValueChange = onGradeChange,
                label = { Text("Grade (0-100)")},
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: Create a Button to "Add Student"
            // Set onClick to 'onAddStudent' and enabled state based on 'name.isNotBlank() && grade.isNotBlank()'
            // Use Modifier.fillMaxWidth()
            Button(
                onClick = onAddStudent,
                enabled = name.isNotBlank() && grade.isNotBlank(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text("Add Student")
            }
        }
    }
}

@Composable
fun StudentsList(
    students: List<Student>, // TODO: Change Any to Student after defining Student data class
    onRemoveStudent: (Student) -> Unit // TODO: Change Any to Student after defining Student data class
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .heightIn(max = 300.dp) // Limit height to prevent overflow
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // TODO: Add a Text composable for "Students (${students.size})"
            // Use style MaterialTheme.typography.titleMedium and Modifier.padding(bottom = 8.dp)
            Text(
                text = "Students (${students.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            if (students.isEmpty()) {
                // TODO: Add a Text composable for "No students added yet" if the list is empty
                // Use style MaterialTheme.typography.bodyMedium and color MaterialTheme.colorScheme.onSurfaceVariant
                Text(
                    text = "No students added yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )


            } else {
                // TODO: Create a LazyColumn to display the list of students
                // Set modifier to Modifier.heightIn(max = 200.dp) and verticalArrangement to Arrangement.spacedBy(4.dp)
                // Inside items, call StudentRow for each student and a Divider if it's not the last student
                LazyColumn(
                    modifier = Modifier.heightIn(max = 200.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    itemsIndexed(students) { index, student ->

                        StudentRow(
                            student = student,
                            onRemoveStudent = onRemoveStudent
                        )

                        if (index < students.lastIndex) {
                            Divider()
                        }
                    }
                }
        }}
    }
}

@Composable
fun StudentRow(
    student: Student, // TODO: Change Any to Student after defining Student data class
    onRemoveStudent: (Student) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            // TODO: Add a Text composable to display student.name
            // Use style MaterialTheme.typography.bodyLarge
            Text(
                text = student.name,
                style = MaterialTheme.typography.bodyLarge
            )

            // TODO: Add a Text composable to display "Grade: ${student.grade}"
            // Use style MaterialTheme.typography.bodyMedium and color MaterialTheme.colorScheme.onSurfaceVariant
            Text(
                text = "Grade: ${student.grade}",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // TODO: Create an IconButton with Icons.Default.Delete for removing a student
        // Set onClick to 'onRemove'
        IconButton(
            onClick = {onRemoveStudent(student)}
        ) {
                Icon(imageVector = Icons.Default.Delete,
                    contentDescription = "Remove Student"
                    )

        }


        }
    }


/**
 * Preview function for the StudentGradeManager screen
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudentGradeManagerPreview() {
    MaterialTheme {
        // TODO: Call StudentGradeManager here for preview
        StudentGradeManager()
    }
}
/**
* How does the MainViewModel effectively manage the various pieces of data (e.g., student list, loading status, input fields) that need to be observed and updated by the UI?
*Explain how the interaction between the StudentGradeManager Composable and the MainViewModel demonstrates a unidirectional data flow. Why is this pattern beneficial for UI development?
*Describe the lifecycle of the MainViewModel in relation to MainActivity. Why is it important for the ViewModel to outlive configuration changes, and how does viewModel() assist with this?
*Explain the key advantages of using LazyColumn for displaying the list of students instead of a regular Column with a scroll modifier. When would you choose one over the other?
*How does the design of this application (with MainActivity, MainViewModel, and various @Composable functions) exemplify the principle of separation of concerns? What are the benefits of this approach?
*/
