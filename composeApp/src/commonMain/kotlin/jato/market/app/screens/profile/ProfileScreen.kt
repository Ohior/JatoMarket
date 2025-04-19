package jato.market.app.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil3.CoilImage
import compose.icons.FeatherIcons
import compose.icons.feathericons.ArrowLeft
import compose.icons.feathericons.Edit3
import compose.icons.feathericons.Home
import compose.icons.feathericons.Save
import jato.app.jato_utils.JDevice
import jato.app.jato_utils.getIfNotNull
import jato.app.jato_utils.ifNotNull
import jato.app.jato_utils.ifNull
import jato.app.jato_utils.rememberJDevice
import jato.market.app.data_model.UserModel
import jato.market.app.theme.EXTRA_LARGE_PADDING
import jato.market.app.theme.HorizontalTextIcon
import jato.market.app.theme.SMALL_PADDING
import jatomarket_.composeapp.generated.resources.Res
import jatomarket_.composeapp.generated.resources.no_image
import org.jetbrains.compose.resources.painterResource

class ProfileScreen(private val userModel: UserModel? = null) : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val device = rememberJDevice()
        val screenModel = rememberScreenModel { ProfileScreenModel() }
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(userModel.getIfNotNull("Jato Market User") { "${it.firstName} ${it.lastName}" }) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surfaceBright),
                    navigationIcon = {
                        IconButton(onClick = { navigator.pop() }) {
                            Icon(FeatherIcons.ArrowLeft, "Go Back")
                        }
                    },
                )
            },
        ) { pv ->
            Box(Modifier.fillMaxSize().padding(pv).padding(SMALL_PADDING)) {
                CreateStorePopup(
                    screenModel.createStorePopup,
                    screenModel
                ) { screenModel.createStorePopup = false }
                when (device) {
                    is JDevice.Landscape -> OwnerDisplayLandscape(device, screenModel)
                    is JDevice.Portrait -> OwnerDisplayPortrait(screenModel)
                    is JDevice.Tablet -> OwnerDisplayTablet(screenModel)
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CreateStorePopup(
        popup: Boolean,
        screenModel: ProfileScreenModel,
        onDismissRequest: () -> Unit = {}
    ) {
        if (!popup) return
        BasicAlertDialog(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.surfaceContainer,
                    MaterialTheme.shapes.medium
                ).padding(SMALL_PADDING),
            onDismissRequest = onDismissRequest,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
            ) {
                Text(
                    text = "Create Store",
                    textDecoration = TextDecoration.Underline,
                    style = MaterialTheme.typography.titleMedium
                )
                TextField(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                    value = screenModel.storeName,
                    onValueChange = { screenModel.storeName = it },
                    label = { Text("Store Name") },
                    singleLine = true,
                    textStyle = MaterialTheme.typography.titleMedium
                )
                TextField(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                    value = screenModel.storeDescription,
                    onValueChange = { screenModel.storeDescription = it },
                    label = { Text("Store Description") },
                    minLines = 3,
                    textStyle = MaterialTheme.typography.titleMedium
                )
                Row {
                    Button(onClick = { screenModel.createStorePopup = false }) {
                        Icon(FeatherIcons.ArrowLeft, "Go Back")
                    }
                    Spacer(Modifier.width(EXTRA_LARGE_PADDING))
                    Button(onClick = { onDismissRequest();screenModel.saveButtonClicked() }) {
                        Icon(FeatherIcons.Save, "Go Back")
                    }
                }
            }

        }
    }

    @Composable
    private fun OwnerDisplayPortrait(screenModel: ProfileScreenModel) {
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
        ) {
            ProfileDisplay(
                modifier = Modifier.fillMaxWidth(),
                screenModel = screenModel
            )
            ProfileDetails(
                modifier = Modifier.fillMaxWidth(),
                screenModel = screenModel
            )
        }
    }

    @Composable
    private fun OwnerDisplayTablet(screenModel: ProfileScreenModel) {
        Column(
            Modifier.fillMaxWidth(0.7f).verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(SMALL_PADDING)
        ) {
            ProfileDisplay(
                modifier = Modifier.fillMaxWidth(),
                screenModel = screenModel
            )
            ProfileDetails(
                modifier = Modifier.fillMaxWidth(),
                screenModel = screenModel
            )
        }
    }

    @Composable
    private fun OwnerDisplayLandscape(device: JDevice.Landscape, screenModel: ProfileScreenModel) {
        val (horizontal1, horizontal2) = remember(device.width) {
            val w1 = device.width / 3
            val w2 = device.width - w1
            w1 to w2
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            // Profile Image
            ProfileDisplay(
                modifier = Modifier.width(horizontal1.dp).verticalScroll(rememberScrollState()),
                screenModel = screenModel
            )
            // Profile Details
            ProfileDetails(
                modifier = Modifier.width(horizontal2.dp).verticalScroll(rememberScrollState()),
                screenModel = screenModel
            )
        }
    }

    @Composable
    private fun ProfileDetails(modifier: Modifier, screenModel: ProfileScreenModel) {
        Column(
            modifier,
            verticalArrangement = Arrangement.spacedBy(SMALL_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Profile Details",
                style = MaterialTheme.typography.titleMedium,
                textDecoration = TextDecoration.Underline
            )
            TextField(
                modifier = Modifier.fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (screenModel.readOnly) Color.Yellow else Color.Green
                    ),
                value = screenModel.firstName,
                onValueChange = { screenModel.firstName = it },
                label = { Text("First Name") },
                singleLine = true,
                readOnly = screenModel.readOnly,
                textStyle = MaterialTheme.typography.titleMedium
            )
            TextField(
                modifier = Modifier.fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (screenModel.readOnly) Color.Yellow else Color.Green
                    ),
                value = screenModel.lastName,
                onValueChange = { screenModel.lastName = it },
                label = { Text("Last Name") },
                singleLine = true,
                readOnly = screenModel.readOnly,
                textStyle = MaterialTheme.typography.titleMedium
            )
            TextField(
                modifier = Modifier.fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = if (screenModel.readOnly) Color.Yellow else Color.Green
                    ),
                value = screenModel.email,
                onValueChange = { screenModel.email = it },
                label = { Text("Email") },
                singleLine = true,
                readOnly = screenModel.readOnly,
                textStyle = MaterialTheme.typography.titleMedium
            )
            if (screenModel.store != null) {
                Text(
                    "Store Details",
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = TextDecoration.Underline
                )
                TextField(
                    modifier = Modifier.fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (screenModel.readOnly) Color.Yellow else Color.Green
                        ),
                    value = screenModel.storeName,
                    onValueChange = { screenModel.storeName = it },
                    label = { Text("Store Name") },
                    singleLine = true,
                    readOnly = screenModel.readOnly,
                    textStyle = MaterialTheme.typography.titleMedium
                )
                TextField(
                    modifier = Modifier.fillMaxWidth()
                        .border(
                            width = 1.dp,
                            color = if (screenModel.readOnly) Color.Yellow else Color.Green
                        ),
                    value = screenModel.storeDescription,
                    onValueChange = { screenModel.storeDescription = it },
                    label = { Text("Store Description") },
                    minLines = 3,
                    readOnly = screenModel.readOnly,
                    textStyle = MaterialTheme.typography.titleMedium
                )
            }
        }
    }

    @Composable
    private fun ProfileDisplay(modifier: Modifier, screenModel: ProfileScreenModel) {
//        val user by screenModel.user.collectAsState()
        Column(
            modifier,
            verticalArrangement = Arrangement.spacedBy(SMALL_PADDING),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            screenModel.imageUrl.ifNotNull {
                CoilImage(
                    modifier = Modifier.fillMaxWidth(),
                    imageModel = { screenModel.imageUrl },
                    imageOptions = ImageOptions(
                        contentScale = ContentScale.FillBounds,
                    )
                )
            }.ifNull {
                Image(
                    painter = painterResource(Res.drawable.no_image),
                    contentDescription = "No image"
                )
            }
            Text(
                "${screenModel.firstName} ${screenModel.lastName}",
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(SMALL_PADDING * 5),
                verticalArrangement = Arrangement.spacedBy(SMALL_PADDING),
            ) {
                HorizontalTextIcon(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(SMALL_PADDING)
                        .clickable { screenModel.readOnly = !screenModel.readOnly },
                    text = "Edit Profile",
                    leadingIcon = { Icon(FeatherIcons.Edit3, "Toggle text editing") },
                    textStyle = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W300)
                )
                HorizontalTextIcon(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceContainer,
                            MaterialTheme.shapes.small
                        )
                        .padding(SMALL_PADDING)
                        .clickable { screenModel.saveButtonClicked() },
                    text = "Update Profile",
                    leadingIcon = { Icon(FeatherIcons.Save, "update or save user profile") },
                    textStyle = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W300)
                )
            }

            screenModel.user.ifNotNull { user ->
                if (user.storeUid == null || user.store == null) {
                    HorizontalTextIcon(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colorScheme.surfaceContainer,
                                MaterialTheme.shapes.small
                            )
                            .padding(SMALL_PADDING)
                            .clickable {
                                screenModel.createStorePopup = !screenModel.createStorePopup
                            },
                        text = "Create Store",
                        leadingIcon = { Icon(FeatherIcons.Home, "create store") },
                        textStyle = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.W300)
                    )
                }
            }
        }
    }
}