<script setup lang="ts">
import { Shield } from 'lucide-vue-next'

defineProps<{
  email?: string
}>()
</script>

<template>
  <header
    class="fixed top-0 left-0 w-full z-50 bg-[#0a0a0a]/80 backdrop-blur-md border-b border-white/5"
  >
    <nav class="max-w-5xl mx-auto px-6 h-16 flex items-center justify-between">
      <div
        class="flex items-center gap-2 group cursor-pointer"
        @click="window.scrollTo({ top: 20, behavior: 'smooth' })"
      >
        <div
          class="w-8 h-8 bg-emerald-500/10 border border-emerald-500/20 rounded flex items-center justify-center group-hover:border-emerald-500/50 transition-colors"
        >
          <Shield :size="16" class="text-emerald-500" />
        </div>
        <span class="font-mono text-sm font-bold tracking-tighter text-white uppercase">
          Thibaut<span class="text-emerald-500">.portfolio</span>
        </span>
      </div>

      <div class="hidden md:flex items-center gap-8">
        <a
          href="#profile"
          class="text-[10px] font-mono uppercase tracking-[0.2em] text-gray-500 hover:text-emerald-400 transition-colors"
          >Profil</a
        >
        <a
          href="#projects"
          class="text-[10px] font-mono uppercase tracking-[0.2em] text-gray-500 hover:text-emerald-400 transition-colors"
          >Projets</a
        >

        <a
          href="#formation"
          class="text-[10px] font-mono uppercase tracking-[0.2em] text-gray-500 hover:text-emerald-400 transition-colors"
          >Formation</a
        >
        <a
          href="#experiences"
          class="text-[10px] font-mono uppercase tracking-[0.2em] text-gray-500 hover:text-emerald-400 transition-colors"
          >Expériences</a
        >
        <a
          href="#softskills"
          class="text-[10px] font-mono uppercase tracking-[0.2em] text-gray-500 hover:text-emerald-400 transition-colors"
          >Soft Skills</a
        >

        <a
          v-if="email"
          :href="'mailto:' + email"
          class="px-4 py-1.5 rounded-lg bg-white/5 border border-white/10 text-[10px] font-mono text-emerald-400 hover:bg-emerald-500/10 hover:border-emerald-500/50 transition-all uppercase tracking-widest"
        >
          Contact
        </a>
      </div>
    </nav>
  </header>
</template>
