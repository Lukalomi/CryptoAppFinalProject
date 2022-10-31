package com.example.cryptoappfinalproject.presentation.ui.chat

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoappfinalproject.databinding.FragmentChatBinding
import com.example.cryptoappfinalproject.domain.model.FirebaseUserModel
import com.example.cryptoappfinalproject.presentation.ui.adapters.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

class ChatFragment : Fragment() {


    private var binding: FragmentChatBinding? = null
    private lateinit var viewModel: ChatViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var auth: FirebaseAuth
val userList:MutableList<FirebaseUserModel> = mutableListOf()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding?.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                if(error != null) {
                    Log.d("dbError",error.message!!)
                }

               for(dc:DocumentChange in value!!.documentChanges) {
                   if(dc.type == DocumentChange.Type.ADDED && !dc.document.toString().contains(auth.currentUser!!.uid)) {

                       userList.add(dc.document.toObject(FirebaseUserModel::class.java))
                   }
               }

                adapter = UserAdapter(requireContext())
                binding!!.rvChat.layoutManager = LinearLayoutManager(requireContext())
                binding!!.rvChat.adapter = adapter
                adapter.onClickListener = {
                    findNavController().navigate(ChatFragmentDirections.actionChatFragmentToChatActivityFragment(it.name))
                }
                adapter.submitList(userList)

            }

        })


    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        userList.clear()
    }
}