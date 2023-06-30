package com.android.tokobuku.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.tokobuku.R
import com.android.tokobuku.application.BookApp
import com.android.tokobuku.databinding.FragmentSecondBinding
import com.android.tokobuku.model.Book

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private  val  bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((applicationContext as BookApp).repository)
    }
    private val args: SecondFragmentArgs by navArgs()
    private var book: Book? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        book = args.book
        if (book != null){
            binding.button.visibility = View.VISIBLE
            binding.saveButton.text = "ubah"
            binding.nameEditText.setText(book?.name)
            binding.addresEditText.setText(book?.address)
            binding.ownerEditText.setText(book?.owner)
        }
        val name = binding.nameEditText.text
        val address = binding.addresEditText.text
        val owner = binding.ownerEditText.text
        binding.saveButton.setOnClickListener {
                if (name.isEmpty()) {
                    Toast.makeText(context, "Nama Toko Buku Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                } else if (address.isEmpty()){
                    Toast.makeText(context, "Alamat Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
                } else if (owner.isEmpty()){
                    Toast.makeText(context, "Nama Pemilik Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else {
                if (book == null){

                    val book = Book(0, name.toString(), address.toString(), owner.toString())
                    bookViewModel.insert(book)
                } else {
                    val book = Book(book?.id!!, name.toString(), address.toString(), owner.toString())
                    bookViewModel.update(book)
                }
                findNavController().popBackStack()
            }

        }

        binding.button.setOnClickListener {
            book?.let {bookViewModel.delete(it) }
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}