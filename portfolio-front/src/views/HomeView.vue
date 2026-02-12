<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getPortfolio } from '../services/portfolioService'
import type { PortfolioDTO } from '../types/PortfolioDTO'
import { downloadCV } from '../services/portfolioService'

const portfolio = ref<PortfolioDTO | null>(null)
const loading = ref(true)

// Petit helper de sécurité pour s'assurer que l'URL est bien formée
const formatUrl = (url: string | undefined) => {
  if (!url) return '#'
  return url.startsWith('http') ? url : `https://${url}`
}

onMounted(async () => {
  try {
    portfolio.value = await getPortfolio()
    console.log('Données chargées :', portfolio.value)
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

    // 1. Créer une URL temporaire pour le Blob
    const url = window.URL.createObjectURL(new Blob([blob]))

    // 2. Créer un élément <a> invisible
    const link = document.createElement('a')
    link.href = url

    // 3. Définir le nom du fichier
    link.setAttribute('download', 'CV_Thibaut_Debreux.pdf')

    // 4. L'ajouter au document, cliquer, et le supprimer
    document.body.appendChild(link)
    link.click()
    link.remove()

    // 5. Libérer la mémoire
    window.URL.revokeObjectURL(url)
  } catch (error) {
    console.error('Erreur lors du téléchargement du PDF :', error)
    alert('Impossible de télécharger le CV.')
  }
}
</script>

<template>
  <div v-if="loading" class="loader">
    <div class="spinner"></div>
    <p>Initialisation du système sécurisé...</p>
  </div>

  <main v-else-if="portfolio" class="container">
    <section id="profile" class="hero">
      <div class="profile-header">
        <img
          v-if="portfolio.profile.photo"
          :src="portfolio.profile.photo"
          class="avatar"
          alt="Photo de profil"
        />
        <div class="profile-info">
          <h1>{{ portfolio.profile.firstName }} {{ portfolio.profile.lastName }}</h1>
          <h2 class="title-gradient">{{ portfolio.profile.title }}</h2>
          <p class="bio">{{ portfolio.profile.bio }}</p>

          <div class="social-links">
            <a
              :href="formatUrl(portfolio.profile.githubUrl)"
              target="_blank"
              rel="noopener noreferrer"
              class="link-item"
            >
              <span class="icon">🔗</span> GitHub
            </a>
            <a
              :href="formatUrl(portfolio.profile.linkedinUrl)"
              target="_blank"
              rel="noopener noreferrer"
              class="link-item"
            >
              <span class="icon">🔗</span> LinkedIn
            </a>
            <a
              :href="formatUrl(portfolio.profile.tryHackMeUrl)"
              target="_blank"
              rel="noopener noreferrer"
              class="link-item"
            >
              <span class="icon">🛡️</span> TryHackMe
            </a>
            <button @click="handleDownload" class="btn-cv">📄 Télécharger CV</button>
          </div>
        </div>
      </div>
    </section>

    <section id="projects" class="section-padding">
      <h3 class="section-title">Projets Récents</h3>
      <div class="grid">
        <div v-for="project in portfolio.projects" :key="project.title" class="card">
          <div class="card-content">
            <h4>{{ project.title }}</h4>
            <p class="summary">{{ project.summary }}</p>
            <div class="tech-stack">
              <span v-for="tech in project.technologies" :key="tech" class="badge">
                {{ tech }}
              </span>
            </div>
            <div class="card-footer">
              <a
                v-if="project.githubUrl"
                :href="formatUrl(project.githubUrl)"
                target="_blank"
                rel="noopener"
                >Code Source</a
              >
            </div>
          </div>
        </div>
      </div>
    </section>
  </main>

  <div v-else class="error-state">
    <p>Erreur de communication avec le serveur (Vérifie le backend et le CORS).</p>
  </div>
</template>

<style scoped>
/* STRUCTURE */
.container {
  max-width: 1100px;
  margin: 0 auto;
  padding: 2rem;
  color: #2d3436;
}

.hero {
  background: #ffffff;
  padding: 3rem;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.05);
  margin-bottom: 3rem;
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 3rem;
  flex-wrap: wrap;
}

/* TYPOGRAPHIE */
h1 {
  font-size: 2.5rem;
  margin: 0;
}
.title-gradient {
  font-size: 1.5rem;
  background: linear-gradient(90deg, #6c5ce7, #a29bfe);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0.5rem 0 1.5rem 0;
}
.bio {
  line-height: 1.6;
  color: #636e72;
  max-width: 600px;
}

/* LIENS & BOUTONS */
.social-links {
  display: flex;
  gap: 1.5rem;
  margin-top: 2rem;
  flex-wrap: wrap;
}

.link-item {
  text-decoration: none;
  color: #2d3436;
  font-weight: 500;
  transition: color 0.3s;
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.link-item:hover {
  color: #6c5ce7;
}

.btn-cv {
  background: #6c5ce7;
  color: white;
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  text-decoration: none;
  font-weight: bold;
  transition:
    transform 0.2s,
    background 0.3s;
}

.btn-cv:hover {
  background: #5b4bc4;
  transform: translateY(-2px);
}

/* GRILLE DE PROJETS */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 2rem;
}

.card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid #edf2f7;
  transition: transform 0.3s;
}

.card:hover {
  transform: translateY(-5px);
}

.card-content {
  padding: 1.5rem;
}

.badge {
  background: #f1f2f6;
  color: #57606f;
  padding: 0.3rem 0.7rem;
  border-radius: 20px;
  font-size: 0.75rem;
  margin-right: 0.5rem;
  margin-top: 0.5rem;
  display: inline-block;
}

.avatar {
  width: 180px;
  height: 180px;
  border-radius: 50%;
  object-fit: cover;
  border: 4px solid #f1f2f6;
}

/* LOADER */
.loader {
  text-align: center;
  margin-top: 100px;
}
.spinner {
  border: 4px solid rgba(0, 0, 0, 0.1);
  width: 36px;
  height: 36px;
  border-radius: 50%;
  border-left-color: #6c5ce7;
  animation: spin 1s linear infinite;
  display: inline-block;
}
@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}
</style>
