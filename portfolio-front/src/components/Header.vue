<script setup lang="ts">
import { ref } from 'vue'
import { Shield, X, Mail, Phone, User, Copy, Check } from 'lucide-vue-next'

// On récupère toutes les infos nécessaires depuis le parent
const props = defineProps<{
  email?: string
  phone?: string
  firstName?: string
  lastName?: string
}>()

const isContactOpen = ref(false)
const copied = ref(false)

// Petit bonus : Copier l'email au clic
const copyEmail = () => {
  if (props.email) {
    navigator.clipboard.writeText(props.email)
    copied.value = true
    setTimeout(() => (copied.value = false), 2000)
  }
}
</script>

<template>
  <header
    class="fixed top-0 left-0 w-full z-50 bg-[#0a0a0a]/80 backdrop-blur-md border-b border-white/5"
  >
    <nav class="max-w-5xl mx-auto px-6 h-16 flex items-center justify-between">
      <a href="#profile" class="flex items-center gap-2 group cursor-pointer">
        <span class="font-mono text-sm font-bold tracking-tighter text-white uppercase">
          Thibaut<span class="text-emerald-500">.portfolio</span>
        </span>
      </a>

      <div class="hidden md:flex items-center gap-8">
        <a href="#profile" class="nav-link">Profil</a>
        <a href="#projects" class="nav-link">Projets</a>
        <a href="#formation" class="nav-link">Formation</a>
        <a href="#experiences" class="nav-link">Expériences</a>
        <a href="#softskills" class="nav-link">Soft Skills</a>

        <button
          @click="isContactOpen = true"
          class="px-4 py-1.5 rounded-lg bg-white/5 border border-white/10 text-[10px] font-mono text-emerald-400 hover:bg-emerald-500/10 hover:border-emerald-500/50 transition-all uppercase tracking-widest cursor-pointer"
        >
          Contact
        </button>
      </div>
    </nav>
  </header>

  <div v-if="isContactOpen" class="fixed inset-0 z-[100] flex items-center justify-center p-4">
    <div class="absolute inset-0 bg-black/80 backdrop-blur-sm" @click="isContactOpen = false"></div>

    <div
      class="relative bg-[#0f0f0f] border border-white/10 rounded-2xl w-full max-w-md shadow-2xl p-6 transform transition-all animate-in fade-in zoom-in-95 duration-200"
    >
      <div class="flex justify-between items-center mb-6 border-b border-white/5 pb-4">
        <h3 class="text-xl font-bold text-white flex items-center gap-2">
          <Shield class="text-emerald-500" :size="20" />
          Contact_Me
        </h3>
        <button
          @click="isContactOpen = false"
          class="text-gray-500 hover:text-white transition-colors"
        >
          <X :size="24" />
        </button>
      </div>

      <div class="space-y-4">
        <div class="flex items-center gap-4 p-3 rounded-xl bg-white/5 border border-white/5">
          <div class="p-2 bg-emerald-500/10 rounded-lg text-emerald-500">
            <User :size="20" />
          </div>
          <div>
            <p class="text-[10px] uppercase text-gray-500 font-mono tracking-wider">Identité</p>
            <p class="text-white font-medium">{{ firstName }} {{ lastName }}</p>
          </div>
        </div>

        <div
          class="flex items-center justify-between gap-4 p-3 rounded-xl bg-white/5 border border-white/5 group"
        >
          <div class="flex items-center gap-4">
            <div class="p-2 bg-emerald-500/10 rounded-lg text-emerald-500">
              <Mail :size="20" />
            </div>
            <div>
              <p class="text-[10px] uppercase text-gray-500 font-mono tracking-wider">Email</p>
              <a
                :href="'mailto:' + email"
                class="text-white font-medium hover:text-emerald-400 transition-colors"
              >
                {{ email }}
              </a>
            </div>
          </div>
          <button @click="copyEmail" class="text-gray-500 hover:text-white" title="Copier">
            <Check v-if="copied" class="text-emerald-500" :size="18" />
            <Copy v-else :size="18" />
          </button>
        </div>

        <div class="flex items-center gap-4 p-3 rounded-xl bg-white/5 border border-white/5">
          <div class="p-2 bg-emerald-500/10 rounded-lg text-emerald-500">
            <Phone :size="20" />
          </div>
          <div>
            <p class="text-[10px] uppercase text-gray-500 font-mono tracking-wider">Téléphone</p>
            <a
              :href="'tel:' + phone"
              class="text-white font-medium hover:text-emerald-400 transition-colors"
            >
              {{ phone }}
            </a>
          </div>
        </div>
      </div>

      <div class="mt-8 text-center">
        <p class="text-[10px] text-gray-600 font-mono">
          STATUS: <span class="text-emerald-500">ONLINE</span> & READY TO WORK
        </p>
      </div>
    </div>
  </div>
</template>

<style scoped>
@reference "../assets/main.css";
.nav-link {
  @apply text-[10px] font-mono uppercase tracking-[0.2em] text-gray-500 hover:text-emerald-400 transition-colors;
}
</style>
