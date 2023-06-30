package com.android.tokobuku.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.tokobuku.R
import com.android.tokobuku.application.BookApp
import com.android.tokobuku.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null

    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private  val  bookViewModel: BookViewModel by viewModels {
        BookViewModelFactory((applicationContext as BookApp).repository)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapater = BookListAdapater{ book ->
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(book)
            findNavController().navigate(action)
        }

        binding.dataRecyclerView.adapter = adapater
        binding.dataRecyclerView.layoutManager = LinearLayoutManager(context)
        bookViewModel.allBook.observe(viewLifecycleOwner){ books ->
            books.let {
                if (books.isEmpty()) {
                    binding.emptyTextView.visibility = View.VISIBLE
                    binding.illustrationimageView.visibility = View.VISIBLE
                } else {
                    binding.emptyTextView.visibility = View.GONE
                    binding.illustrationimageView.visibility = View.GONE
                }
                adapater.submitList(books)
            }
        }

        binding.addFAB.setOnClickListener {
            val action = FirstFragmentDirections.actionFirstFragmentToSecondFragment(null)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}