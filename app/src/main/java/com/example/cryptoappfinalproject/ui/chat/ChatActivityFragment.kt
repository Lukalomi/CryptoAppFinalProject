package com.example.cryptoappfinalproject.ui.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptoappfinalproject.databinding.FragmentChatActivityBinding
import com.example.cryptoappfinalproject.domain.FirebaseUser
import com.example.cryptoappfinalproject.domain.MessageModel
import com.example.cryptoappfinalproject.ui.adapters.MessageAdapter
import com.example.cryptoappfinalproject.ui.adapters.UserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*


class ChatActivityFragment : Fragment() {

    private var binding: FragmentChatActivityBinding? = null
    private lateinit var adapter: MessageAdapter
    private val messageList: MutableList<MessageModel> = mutableListOf()
    private val args: ChatActivityFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatActivityBinding.inflate(inflater, container, false)

        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        goBack()
        binding!!.tvUserChat.text = args.uid.toString()

        adapter = MessageAdapter()
        binding!!.rvChatActivity.layoutManager = LinearLayoutManager(requireContext())
        binding!!.rvChatActivity.adapter = adapter
        adapter.submitList(messageList.asReversed())
        addMessagesToList()
        sendDM()
    }

    private fun addMessagesToList() {
        val db = FirebaseFirestore.getInstance()
                db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("chats")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.d("dbError", error.message!!)
                    }
                    for (dc: DocumentChange in value!!.documentChanges) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            messageList.add(dc.document.toObject(MessageModel::class.java))
                            Log.d("dms",messageList.toString())

                        }
                        adapter = MessageAdapter()
                        binding!!.rvChatActivity.layoutManager = LinearLayoutManager(requireContext())
                        binding!!.rvChatActivity.adapter = adapter

                        adapter.submitList(messageList)
                    }
                }
            })

    }


    private fun goBack() {
        binding!!.ivBackChat.setOnClickListener {
            findNavController().navigate(ChatActivityFragmentDirections.actionChatActivityFragmentToChatFragment())
        }

    }

    private fun sendDM() {
        val senderUid = FirebaseAuth.getInstance().currentUser?.uid
            binding!!.etMessage.onRightDrawableClicked {
                val messageObject = MessageModel(it.text.toString(), senderUid)
                if(it.text.toString() != "") {
                    val db = FirebaseFirestore.getInstance()

                    db.collection("users").document(FirebaseAuth.getInstance().currentUser!!.uid).collection("chats")
                        .add(messageObject)
                        .addOnSuccessListener { nothing ->
                            try {
                                adapter = MessageAdapter()
                                binding!!.rvChatActivity.layoutManager =
                                    LinearLayoutManager(requireContext())
                                binding!!.rvChatActivity.adapter = adapter

                                adapter.submitList(messageList)
                                Toast.makeText(
                                    requireContext(),
                                    "message has been sent",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }catch (e:Exception) {
                                Log.d("dbError",e.message.toString())
                            }


                        }.addOnFailureListener {
                            try{
                                Toast.makeText(
                                    requireContext(),
                                    "can't send a dm",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }catch (e:Exception) {
                                Log.d("dbError",e.message.toString())
                            }
                        }
                }

            }

    }

    private fun EditText.onRightDrawableClicked(onClicked: (view: EditText) -> Unit) {
        this.setOnTouchListener { v, event ->
            var hasConsumed = false
            if (v is EditText) {
                if (event.x >= v.width - v.totalPaddingRight) {
                    if (event.action == MotionEvent.ACTION_UP) {
                        onClicked(this)
                    }
                    hasConsumed = true
                }
            }
            hasConsumed
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        messageList.clear()

    }

}