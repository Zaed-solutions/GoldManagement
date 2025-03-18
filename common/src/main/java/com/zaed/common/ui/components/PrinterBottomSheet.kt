package com.zaed.common.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bluetooth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.dantsu.escposprinter.connection.bluetooth.BluetoothConnection
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.textparser.PrinterTextParserImg
import com.zaed.common.R
import com.zaed.common.data.model.sale.StoreTransaction
import com.zaed.common.util.AsyncBluetoothEscPosPrint
import com.zaed.common.util.AsyncEscPosPrint
import com.zaed.common.util.AsyncEscPosPrinter
import com.zaed.common.util.getAsyncEscPosPrinter
import kotlinx.coroutines.launch
import org.checkerframework.checker.units.qual.s

private val TAG = "PrinterBottomSheet"

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("MissingPermission")
@Composable
fun PrinterBottomSheet(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onDismiss: () -> Unit,
    sale: StoreTransaction,
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(false) }
    var selectedDevice by remember { mutableStateOf<BluetoothConnection?>(null) }
    var selectedDeviceName by remember { mutableStateOf("Select Printer") }
    var showDeviceDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var printedSuccessfully by remember { mutableStateOf(false) }
    var printedError by remember { mutableStateOf(false) }
    // State to track permission status
    var permissionsGranted by remember { mutableStateOf(false) }

    // Define checkNextPermission function reference for recursive permission checking
    var checkNextPermissionRef by remember { mutableStateOf<() -> Unit>({}) }

    // Define permission request launchers
    val requestBluetoothPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "PrinterBottomSheet: bluetooth permission granted")
            checkNextPermissionRef()
        }
    }

    val requestBluetoothAdminPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "PrinterBottomSheet: bluetooth admin permission granted")
            checkNextPermissionRef()
        }
    }

    val requestBluetoothConnectPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "PrinterBottomSheet: bluetooth connect permission granted")
            checkNextPermissionRef()
        }
    }

    val requestBluetoothScanPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Log.d(TAG, "PrinterBottomSheet: bluetooth scan permission granted")
            permissionsGranted = true
        }
    }

    // Initialize the checkNextPermission function
    checkNextPermissionRef = {
        when {
            Build.VERSION.SDK_INT < Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH
                    ) != PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "PrinterBottomSheet: bluetooth permission not granted")
                requestBluetoothPermission.launch(Manifest.permission.BLUETOOTH)
            }

            Build.VERSION.SDK_INT < Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_ADMIN
                    ) != PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "PrinterBottomSheet: bluetooth admin permission not granted")
                requestBluetoothAdminPermission.launch(Manifest.permission.BLUETOOTH_ADMIN)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "PrinterBottomSheet: bluetooth connect permission not granted")
                requestBluetoothConnectPermission.launch(Manifest.permission.BLUETOOTH_CONNECT)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_SCAN
                    ) != PackageManager.PERMISSION_GRANTED -> {
                Log.d(TAG, "PrinterBottomSheet: bluetooth scan permission not granted")
                requestBluetoothScanPermission.launch(Manifest.permission.BLUETOOTH_SCAN)
            }

            else -> {
                permissionsGranted = true
            }
        }
    }

    // Function to browse Bluetooth devices
    fun browseBluetoothDevices() {
        if (permissionsGranted) {
            Log.d("PrinterBottomSheet", "browseBluetoothDevices: permissions granted")
            val bluetoothDevicesList = BluetoothPrintersConnections().list

            if (bluetoothDevicesList != null && bluetoothDevicesList.isNotEmpty()) {
                showDeviceDialog = true
            } else {
                // Show a snackbar or toast that no devices were found
                Toast.makeText(
                    context,
                    context.getString(R.string.no_bluetooth_devices_found),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Log.d("PrinterBottomSheet", "browseBluetoothDevices: permission not granted")
            checkNextPermissionRef()
        }
    }

    // Function to print via Bluetooth
    fun printBluetooth() {
        if (permissionsGranted && selectedDevice != null) {
            scope.launch {
                AsyncBluetoothEscPosPrint(
                    context,
                    object : AsyncEscPosPrint.OnPrintFinished() {
                        override fun onError(
                            asyncEscPosPrinter: AsyncEscPosPrinter?,
                            codeException: Int
                        ) {
                            Log.e("Async.OnPrintFinished", "An error occurred!")
                            printedError = true
                        }

                        override fun onSuccess(asyncEscPosPrinter: AsyncEscPosPrinter?) {
                            Log.i("Async.OnPrintFinished", "Print is finished!")
                            printedSuccessfully = true
                        }
                    }
                ).execute(
                    getAsyncEscPosPrinter(
                        context = context,
                        printerConnection = selectedDevice!!,
                        sale = sale
                    )
                )
            }
        } else if (selectedDevice == null) {
            Toast.makeText(
                context,
                context.getString(R.string.no_bluetooth_device_selected),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            checkNextPermissionRef()
        }
    }

    // UI Components
    AnimatedVisibility(isVisible) {
        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState
        ) {
            AnimatedContent(
                FourTuple(showDeviceDialog, printedError, printedSuccessfully, isLoading),
            ) { state ->
                when {
                    state.first -> {
                        // bluetooth devices list
                        val bluetoothDevicesList = BluetoothPrintersConnections().list
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedIconButton(
                                    onClick = {
                                        showDeviceDialog = false
                                    },
                                    modifier = Modifier.align(Alignment.CenterStart)
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                                Text(
                                    text = stringResource(R.string.bluetooth_printer_selection),
                                    style = MaterialTheme.typography.titleLarge,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                            Column {
                                bluetoothDevicesList?.forEach { device ->
                                    Surface(
                                        onClick = {
                                            selectedDevice = device
                                            selectedDeviceName =
                                                device.device.name ?: "Unknown device"
                                            showDeviceDialog = false
                                        },
                                        color = Color.Transparent
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Bluetooth,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.onSecondary,
                                                modifier = Modifier
                                                    .size(40.dp)
                                                    .background(
                                                        color = MaterialTheme.colorScheme.secondary,
                                                        shape = CircleShape
                                                    )
                                                    .padding(8.dp)
                                            )
                                            Text(
                                                device.device.name ?: "Unknown device",
                                                style = MaterialTheme.typography.titleLarge,
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    state.second -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.print_error),
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Button(
                                onClick = { printedError = false },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp)
                            ) {
                                Text(stringResource(R.string.try_again))
                            }
                        }
                    }

                    state.third -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = stringResource(R.string.print_success),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }

                    state.fourth -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(40.dp),
                            )
                            Text(
                                text = stringResource(R.string.printing_in_progress),
                                style = MaterialTheme.typography.headlineMedium
                            )
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.print_receipt),
                                style = MaterialTheme.typography.headlineMedium
                            )

                            Button(
                                onClick = {

                                    browseBluetoothDevices() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 24.dp)
                                    .heightIn(min = 48.dp)
                            ) {
                                Text(
                                    text = if (selectedDevice == null) stringResource(R.string.select_printer) else stringResource(
                                        R.string.selected_printer,
                                        selectedDeviceName
                                    )
                                )
                            }

                            Button(
                                onClick = { printBluetooth() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 48.dp),
                                enabled = selectedDevice != null
                            ) {
                                Text(stringResource(R.string.print))
                            }
                        }
                    }
                }
            }
        }
    }
}