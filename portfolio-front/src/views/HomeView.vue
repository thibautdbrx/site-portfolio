<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getPortfolio, downloadCV } from '../services/portfolioService'
import type { PortfolioDTO } from '../types/PortfolioDTO'
import { Github, Linkedin, Shield, FileText, ExternalLink } from 'lucide-vue-next'

// --- IMPORT DES COMPOSANTS ---
import Header from '../components/Header.vue'
import Footer from '../components/Footer.vue'
import * as https from 'node:https'

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
  <div class="min-h-screen bg-[#0a0a0a] text-gray-300 selection:bg-emerald-500/30">
    <Header :email="portfolio?.profile.email" />

    <div
      v-if="loading"
      class="flex items-center justify-center h-screen font-mono text-emerald-500"
    >
      <span class="animate-pulse tracking-tighter">BOOTING_SYSTEM...</span>
    </div>

    <main v-else-if="portfolio" class="max-w-5xl mx-auto px-6 pt-32 pb-16">
      <section
        id="profile"
        class="mb-24 flex flex-col md:flex-row items-center gap-12 bg-neutral-900/30 p-10 rounded-3xl border border-white/5"
      >
        <div class="relative group">
          <div
            class="absolute -inset-1 bg-emerald-500 rounded-full blur opacity-10 group-hover:opacity-30 transition duration-500"
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
          <h1 class="text-5xl font-bold text-white mb-2">
            {{ portfolio.profile.firstName }}
            <span class="text-emerald-500">{{ portfolio.profile.lastName }}</span>
          </h1>
          <p class="text-emerald-400 font-mono text-sm uppercase tracking-widest mb-6 italic">
            // {{ portfolio.profile.title }}
          </p>
          <p class="text-gray-400 leading-relaxed mb-8">{{ portfolio.profile.bio }}</p>

          <div class="flex flex-wrap gap-3 justify-center md:justify-start">
            <a
              :href="formatUrl(portfolio.profile.githubUrl)"
              target="_blank"
              class="flex items-center gap-2 px-4 py-2 rounded-xl border border-white/10 hover:border-emerald-500/50 hover:bg-white/5 transition-all text-sm font-medium text-gray-400 hover:text-white group"
            >
              <Github :size="18" class="group-hover:text-emerald-400 transition-colors" />
              <span>GitHub</span>
            </a>
            <a
              :href="formatUrl(portfolio.profile.linkedinUrl)"
              target="_blank"
              class="flex items-center gap-2 px-4 py-2 rounded-xl border border-white/10 hover:border-emerald-500/50 hover:bg-white/5 transition-all text-sm font-medium text-gray-400 hover:text-white group"
            >
              <Linkedin :size="18" class="group-hover:text-emerald-400 transition-colors" />
              <span>LinkedIn</span>
            </a>
            <a
              :href="formatUrl(portfolio.profile.tryHackMeUrl)"
              target="_blank"
              class="flex items-center gap-2 px-4 py-2 rounded-xl border border-emerald-500/10 hover:border-emerald-500/50 hover:bg-emerald-500/5 transition-all text-sm font-medium text-emerald-500/80 hover:text-emerald-400 group"
            >
              <Shield :size="18" class="group-hover:text-emerald-400 transition-colors" />
              <span>TryHackMe</span>
            </a>
            <button
              @click="handleDownload"
              class="flex items-center gap-2 px-6 py-2.5 bg-emerald-600 hover:bg-emerald-500 text-white rounded-xl font-bold text-sm transition-all shadow-lg shadow-emerald-900/20 active:scale-95"
            >
              <FileText :size="18" />
              Télécharger CV
            </button>
          </div>
        </div>
      </section>

      <section id="projects" class="mb-24">
        <h3 class="text-xl font-bold text-white mb-10 flex items-center gap-3 italic">
          <span class="text-emerald-500 font-mono">01.</span> Projets_
        </h3>
        <div class="grid grid-cols-1 md:grid-cols-1 gap-6">
          <div
            v-for="project in portfolio.projects"
            :key="project.title"
            class="border-white/3 rounded-2xl p-8 hover:border-emerald-500/30 transition-all group relative"
          >
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
                class="text-gray-500 hover:text-emerald-400 transition-colors"
              >
                <ExternalLink :size="20" />
              </a>
            </div>
            <p class="text-gray-400 text-sm mb-8 leading-relaxed">{{ project.summary }}</p>
            <div class="flex flex-wrap gap-2">
              <span
                v-for="tech in project.technologies"
                :key="tech"
                class="text-[10px] font-mono font-bold px-2.5 py-1 rounded bg-emerald-500/5 text-emerald-500 border border-emerald-500/10 uppercase tracking-wider"
              >
                {{ tech }}
              </span>
            </div>
          </div>
        </div>
      </section>

      <section id="formation">
          <h3 class="text-xl font-bold text-white mb-10 flex items-center gap-3 italic">
            <span class="text-emerald-500 font-mono">03.</span> Expériences_
          </h3>
          <div class="grid grid-cols-1 md:grid-cols-1 gap-6">
            <div
              v-for="education in portfolio.educations"
              :key="education.schoolName"
              class="border-white/5 rounded-2xl p-8 hover:border-emerald-500/30 transition-all group relative"
            >
              <div class="flex justify-between items-start">
                <div>
                  <h4
                    class="text-xl font-bold text-white group-hover:text-emerald-400 transition-colors"
                  >
                    {{ education.degree }} {{education.fieldOfStudy}}
                  </h4>
                </div>

                <p class="text-gray-400 text-sm leading-relaxed shrink-0 ml-4">
                  <span>{{ education.startDate }}</span>
                  <span v-if="education.endDate"> - {{ education.endDate }}</span>
                </p>
              </div>


              <div class="flex flex-wrap gap-3">
              <span
                v-for="tech in education.relevantCourses"
                :key="tech"
                class="text-[10px] font-mono font-bold px-2.5 py-1 rounded bg-emerald-500/5 text-emerald-500 border border-emerald-500/10 uppercase tracking-wider"
              >
                {{ tech }}
              </span>
              </div>
            </div>
          </div>
        </section>
      <section id="experiences">
        <h3 class="text-xl font-bold text-white mb-10 flex items-center gap-3 italic">
          <span class="text-emerald-500 font-mono">03.</span> Expériences_
        </h3>
        <div class="grid grid-cols-1 md:grid-cols-1 gap-6">
          <div
            v-for="experience in portfolio.experiences"
            :key="experience.companyName"
            class="border-white/5 rounded-2xl p-8 hover:border-emerald-500/30 transition-all group relative"
          >
            <div class="flex justify-between items-start">
              <div>
                <h4
                  class="text-xl font-bold text-white group-hover:text-emerald-400 transition-colors"
                >
                  {{ experience.companyName }}
                </h4>
                <p class="text-emerald-400 font-medium">{{ experience.position }}</p>
              </div>

              <p class="text-gray-400 text-sm leading-relaxed shrink-0 ml-4">
                <span>{{ experience.startDate }}</span>
                <span v-if="experience.endDate"> - {{ experience.endDate }}</span>
              </p>
            </div>
            <div>
              <p class="flex mb-2">{{ experience.location }}</p>
              <p>{{ experience.description }}</p>
            </div>

            <div class="flex flex-wrap gap-3">
              <span
                v-for="tech in experience.toolsUsed"
                :key="tech"
                class="text-[10px] font-mono font-bold px-2.5 py-1 rounded bg-emerald-500/5 text-emerald-500 border border-emerald-500/10 uppercase tracking-wider"
              >
                {{ tech }}
              </span>
            </div>
          </div>
        </div>
      </section>

      <Footer />
    </main>
  </div>
</template>
