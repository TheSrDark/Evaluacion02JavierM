package com.example.tareajme02c


import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var spinner: Spinner

    data class Item(val title: String, val description: String, val imageResId: Int, val category: String)

    private lateinit var itemList: List<Item>
    private lateinit var adapter: ItemAdapter

    class ItemAdapter(private val itemList: List<Item>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

        private var filteredList: List<Item> = itemList

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTextView: TextView = itemView.findViewById(R.id.textViewTitle)
            val descriptionTextView: TextView = itemView.findViewById(R.id.textViewDescription)
            val imageView: ImageView = itemView.findViewById(R.id.imageViewCard)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
            // Calcula el ancho de cada elemento en la cuadrícula según el número de columnas
            val displayMetrics = parent.context.resources.displayMetrics
            val screenWidth = displayMetrics.widthPixels
            val columns = parent.context.resources.getInteger(R.integer.grid_columns) // Asegúrate de tener un valor en resources/integers
            val itemWidth = screenWidth / columns
            view.layoutParams = ViewGroup.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = filteredList[position]
            holder.titleTextView.text = item.title
            holder.descriptionTextView.text = item.description
            holder.imageView.setImageResource(item.imageResId)
        }

        override fun getItemCount(): Int {
            return filteredList.size
        }

        fun filter(selectedCategory: String) {
            filteredList = if (selectedCategory == "Todas") {
                itemList
            } else {
                itemList.filter { it.category == selectedCategory }
            }
            notifyDataSetChanged()
        }
    }
//-------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        spinner = findViewById(R.id.spinner_categories)
        val categories = listOf("Todas", "Aventura", "Acción", "Terror")

        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerAdapter

        itemList = listOf(
            Item("Señor de los anillos", "Genero: Aventura", R.drawable.img_aventura01, "Aventura"),
            Item("John Wick", "Genero: Acción", R.drawable.img_accion03, "Acción"),
            Item("El Conjuro", "Genero: Terror", R.drawable.img_terror01, "Terror"),
            Item("El Hobbit - La desolación de Smaug", "Genero: Aventura", R.drawable.img_aventura02, "Aventura"),
            Item("Annabelle", "Genero: Terror", R.drawable.img_terror03, "Terror")
        )

        adapter = ItemAdapter(itemList)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categories[position]
                adapter.filter(selectedCategory)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Maneja aquí si es necesario
            }
        }
    }
}
