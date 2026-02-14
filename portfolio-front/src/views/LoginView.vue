<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const username = ref('')
const password = ref('')
const router = useRouter()

const handleLogin = async () => {
  try {
    const response = await axios.post('http://localhost:8080/api/auth/login', {
      username: username.value,
      password: password.value,
    })

    // On stocke le token
    localStorage.setItem('token', response.data.token)

    // On redirige vers l'admin
    router.push('/admin')
  } catch (error) {
    alert('Accès refusé. Tentative de hack détectée ? ;)')
  }
}
</script>

<template>
  <div class="min-h-screen flex items-center justify-center bg-[#050505]">
    <div class="p-8 bg-[#0a0a0a] border border-white/5 rounded-2xl w-full max-w-sm">
      <h2 class="text-2xl font-bold text-white mb-6 font-mono tracking-tighter">
        SYSTEM<span class="text-emerald-500">_AUTH</span>
      </h2>
      <input
        v-model="username"
        type="text"
        placeholder="Utilisateur"
        class="w-full bg-white/5 border border-white/10 p-3 rounded-lg mb-4 outline-none focus:border-emerald-500"
      />
      <input
        v-model="password"
        type="password"
        placeholder="Code d'accès"
        class="w-full bg-white/5 border border-white/10 p-3 rounded-lg mb-6 outline-none focus:border-emerald-500"
      />
      <button
        @click="handleLogin"
        class="w-full bg-emerald-600 p-3 rounded-lg font-bold text-white hover:bg-emerald-500 transition-all"
      >
        S'IDENTIFIER
      </button>
    </div>
  </div>
</template>
