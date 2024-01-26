package msdog.demo_usb_serial

import android.content.Context
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.hoho.android.usbserial.driver.ProbeTable
import com.hoho.android.usbserial.driver.ProlificSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialProber


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tv = findViewById<TextView>(R.id.tv)

        val manager = getSystemService(Context.USB_SERVICE) as UsbManager
        val values = manager.deviceList.values
        val customTable = ProbeTable()
        if (values.isNotEmpty()) {
            val first = values.first()
            val vendorId = first.vendorId
            val productId = first.productId
            Log.e("DDDD", "vendorId: $vendorId ,productId: $productId")

            customTable.addProduct(1356, 3348, ProlificSerialDriver::class.java)
            val prober = UsbSerialProber(customTable)
            // 查找指定的设备是否存在
            val drivers: List<UsbSerialDriver> = prober.findAllDrivers(manager)
            if (drivers.isNotEmpty()) {
                val driver = drivers[0]
                // 这个设备存在，连接到这个设备
                val connection = manager.openDevice(driver.device)
                val port = driver.ports[0]
                port.open(connection)
                // 设置连接参数，波特率9600，以及 “8N1”
                // port.setParameters(9600, 8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE)
                Log.e("DDDD", "连接打开成功！")
            } else {
                Log.e("DDDD", "连接打开失败！")
            }
        }
    }

    private fun scanDevice(context: Context) {
        val manager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val deviceList: HashMap<String, UsbDevice> = manager.deviceList
        Log.e("DDDDD", "scanDevice: $deviceList")
    }
}