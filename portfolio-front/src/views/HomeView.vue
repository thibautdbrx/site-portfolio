<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getPortfolio, downloadCV } from '../services/portfolioService'
import type { PortfolioDTO } from '../types/PortfolioDTO'
import {
  Github,
  Linkedin,
  Shield,
  FileText,
  ExternalLink,
  Calendar,
  MapPin,
  Award,
} from 'lucide-vue-next'

// --- IMPORT DES COMPOSANTS ---
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'

const portfolio = ref<PortfolioDTO | null>(null)
const loading = ref(true)

const FILE_BASE_URL = import.meta.env.VITE_FILE_URL || 'http://localhost:8080/api/files'

const formatUrl = (url: string | undefined) => {
  if (!url) return '#'
  return url.startsWith('http') ? url : `https://${url}`
}

onMounted(async () => {
  try {
    portfolio.value = await getPortfolio()
  } catch (error) {
    console.error('Erreur API:', error)
  } finally {
    loading.value = false
  }
})

const handleDownload = async () => {
  if (!portfolio.value?.profile.cvPdfUrl) return
  try {
    const blob = await downloadCV(portfolio.value.profile.cvPdfUrl)
    const url = window.URL.createObjectURL(new Blob([blob]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', 'CV_Thibaut_Debreux.pdf')
    document.body.appendChild(link)
    link.click()
    link.remove()
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('Erreur Download:', error)
    alert('Erreur lors du téléchargement du CV')
  }
}
</script>

<template>
  <div class="min-h-screen bg-[#0a0a0a] text-gray-300 selection:bg-emerald-500/30 font-sans">
    <Header :email="portfolio?.profile.email" />

    <div
      v-if="loading"
      class="flex items-center justify-center h-screen font-mono text-emerald-500"
    >
      <div class="flex flex-col items-center gap-4">
        <div
          class="w-12 h-12 border-2 border-emerald-500/20 border-t-emerald-500 rounded-full animate-spin"
        ></div>
        <span class="animate-pulse tracking-tighter">BOOTING_SYSTEM...</span>
      </div>
    </div>

    <main v-else-if="portfolio" class="max-w-5xl mx-auto px-6 pt-32 pb-16">
      <section
        id="profile"
        class="mb-24 flex flex-col md:flex-row items-center gap-12 bg-white/5 p-10 rounded-3xl border border-white/5"
      >
        <div class="relative group">
          <div
            class="absolute -inset-1 bg-emerald-500 rounded-full blur opacity-10 group-hover:opacity-25 transition duration-500"
          ></div>
          <img
            v-if="portfolio.profile.avatarUrl"
            :src="
              portfolio.profile.avatarUrl.startsWith('http')
                ? portfolio.profile.avatarUrl
                : FILE_BASE_URL + '/' + portfolio.profile.avatarUrl
            "
            class="relative w-40 h-40 rounded-full object-cover border-2 border-emerald-500/20 shadow-2xl"
            alt="Profil"
          />
        </div>

        <div class="flex-1 text-center md:text-left">
          <h1 class="text-5xl font-bold text-white mb-2 tracking-tight">
            {{ portfolio.profile.firstName }}
            <span class="text-emerald-500">{{ portfolio.profile.lastName }}</span>
          </h1>
          <p class="text-emerald-400 font-mono text-sm uppercase tracking-widest mb-6">
            // {{ portfolio.profile.title }}
          </p>
          <p class="text-gray-400 leading-relaxed mb-8 max-w-2xl">{{ portfolio.profile.bio }}</p>

          <div class="flex flex-wrap gap-3 justify-center md:justify-start">
            <a :href="formatUrl(portfolio.profile.githubUrl)" target="_blank" class="btn-social"
              ><Github :size="18" /> GitHub</a
            >
            <a :href="formatUrl(portfolio.profile.linkedinUrl)" target="_blank" class="btn-social"
              ><Linkedin :size="18" /> LinkedIn</a
            >
            <a
              :href="formatUrl(portfolio.profile.tryHackMeUrl)"
              target="_blank"
              class="btn-social text-emerald-500 border-emerald-500/20"
              ><Shield :size="18" /> TryHackMe</a
            >
            <button @click="handleDownload" class="btn-primary">
              <FileText :size="18" /> Télécharger CV
            </button>
          </div>
        </div>
      </section>

      <section id="projects" class="mb-24 scroll-mt-24">
        <h3 class="section-title"><span class="text-emerald-500 font-mono">01.</span> Projets_</h3>
        <div class="grid grid-cols-1 gap-6">
          <div v-for="project in portfolio.projects" :key="project.title" class="card group">
            <div class="flex justify-between items-start mb-4">
              <h4
                class="text-xl font-bold text-white group-hover:text-emerald-400 transition-colors"
              >
                {{ project.title }}
              </h4>
              <a
                v-if="project.githubUrl"
                :href="formatUrl(project.githubUrl)"
                target="_blank"
                class="text-gray-500 hover:text-emerald-400"
                ><ExternalLink :size="20"
              /></a>
            </div>
            <p class="text-gray-400 text-sm mb-6 leading-relaxed">{{ project.summary }}</p>
            <div class="flex flex-wrap gap-2">
              <span v-for="tech in project.technologies" :key="tech" class="badge-tech">{{
                tech
              }}</span>
            </div>
          </div>
        </div>
      </section>

      <section id="formation" class="mb-24 scroll-mt-24">
        <h3 class="section-title">
          <span class="text-emerald-500 font-mono">02.</span> Formation_
        </h3>
        <div class="space-y-6">
          <div v-for="edu in portfolio.educations" :key="edu.schoolName" class="card group">
            <div class="flex flex-col md:flex-row justify-between mb-4 gap-2">
              <div>
                <h4
                  class="text-xl font-bold text-white group-hover:text-emerald-400 transition-colors"
                >
                  {{ edu.schoolName }}
                </h4>
                <p class="text-emerald-400 font-medium">
                  {{ edu.degree }} <span class="text-gray-600 mx-2">|</span>
                  <span class="text-gray-400 text-sm">{{ edu.fieldOfStudy }}</span>
                </p>
              </div>
              <div
                class="text-left md:text-right font-mono text-xs text-gray-500 uppercase tracking-wider"
              >
                <div class="flex items-center md:justify-end gap-2 mb-1">
                  <MapPin :size="12" /> {{ edu.location }}
                </div>
                <div class="flex items-center md:justify-end gap-2">
                  <Calendar :size="12" /> {{ edu.startDate }} — {{ edu.endDate || 'Présent' }}
                </div>
              </div>
            </div>
            <p class="text-gray-400 text-sm mb-6">{{ edu.description }}</p>
            <div class="flex flex-wrap gap-2">
              <span v-for="course in edu.relevantCourses" :key="course" class="badge-tech">{{
                course
              }}</span>
            </div>
          </div>
        </div>
      </section>

      <section id="experiences" class="mb-24 scroll-mt-24">
        <h3 class="section-title">
          <span class="text-emerald-500 font-mono">03.</span> Expériences_
        </h3>
        <div class="space-y-6">
          <div v-for="exp in portfolio.experiences" :key="exp.companyName" class="card group">
            <div class="flex flex-col md:flex-row justify-between mb-4 gap-2">
              <div>
                <h4
                  class="text-xl font-bold text-white group-hover:text-emerald-400 transition-colors"
                >
                  {{ exp.companyName }}
                </h4>
                <p class="text-emerald-400 font-medium">{{ exp.position }}</p>
              </div>
              <div
                class="text-left md:text-right font-mono text-xs text-gray-500 uppercase tracking-wider"
              >
                <div class="flex items-center md:justify-end gap-2 mb-1">
                  <MapPin :size="12" /> {{ exp.location }}
                </div>
                <div class="flex items-center md:justify-end gap-2">
                  <Calendar :size="12" /> {{ exp.startDate }} — {{ exp.endDate || 'Présent' }}
                </div>
              </div>
            </div>
            <p class="text-gray-400 text-sm mb-6">{{ exp.description }}</p>
            <div class="flex flex-wrap gap-2">
              <span v-for="tool in exp.toolsUsed" :key="tool" class="badge-tech">{{ tool }}</span>
            </div>
          </div>
        </div>
      </section>

      <section id="softskills" class="mb-24 scroll-mt-24">
        <h3 class="section-title"><span class="text-emerald-500 font-mono">04.</span> Skills_</h3>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div
            v-for="skill in portfolio.skills"
            :key="skill.name"
            class="flex items-center justify-between p-4 bg-white/5 border border-white/5 rounded-xl hover:border-emerald-500/30 transition-all"
          >
            <div class="flex items-center gap-3">
              <div class="p-2 bg-emerald-500/10 rounded-lg text-emerald-500">
                <Award :size="18" />
              </div>
              <div>
                <p class="font-bold text-white text-sm tracking-wide">{{ skill.name }}</p>
                <p class="text-[10px] font-mono uppercase text-gray-500 tracking-tighter">
                  {{ skill.category }}
                </p>
              </div>
            </div>
            <span
              class="text-[10px] font-mono px-2 py-0.5 rounded border border-emerald-500/20 text-emerald-400 bg-emerald-500/5"
              >{{ skill.level }}</span
            >
          </div>
        </div>
      </section>

      <Footer />
    </main>
  </div>
</template>

<style scoped>
@reference "../assets/main.css";
.section-title {
  @apply text-xl font-bold text-white mb-10 flex items-center gap-3 italic;
}

.card {
  @apply bg-white/5 border border-white/5 rounded-2xl p-8 hover:border-emerald-500/30 transition-all relative;
}

.btn-social {
  @apply flex items-center gap-2 px-4 py-2 rounded-xl border border-white/10 hover:border-emerald-500/50 hover:bg-white/5 transition-all text-sm font-medium text-gray-400 hover:text-white;
}

.btn-primary {
  @apply flex items-center gap-2 px-6 py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl font-bold text-sm transition-all shadow-lg shadow-emerald-900/20 active:scale-95;
}

.badge-tech {
  @apply text-[10px] font-mono font-bold px-2.5 py-1 rounded bg-emerald-500/5 text-emerald-500 border border-emerald-500/10 uppercase tracking-wider;
}
</style>
