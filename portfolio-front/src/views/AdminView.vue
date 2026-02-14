<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { portfolioService } from '../services/portfolioService'
import type { PortfolioDTO } from '../types/PortfolioDTO'
import {
  LayoutDashboard,
  User,
  Briefcase,
  GraduationCap,
  Code,
  Layers,
  Plus,
  Edit,
  Trash2,
  Save,
  X,
  Upload,
  FileText,
  ArrowLeft,
  Image as ImageIcon,
} from 'lucide-vue-next'

// --- ÉTAT GLOBAL ---
const portfolio = ref<PortfolioDTO | null>(null)
const loading = ref(true)
const activeTab = ref('profile')
const isModalOpen = ref(false)
const modalType = ref('')
const editingItem = ref<any>({})

// --- CHARGEMENT ---
const loadData = async () => {
  loading.value = true
  try {
    portfolio.value = await portfolioService.getPortfolio()
  } catch (error) {
    console.error('Erreur de chargement', error)
  } finally {
    loading.value = false
  }
}

onMounted(loadData)

// --- GESTION DES MODALS ---
const openModal = (type: string, item?: any) => {
  modalType.value = type
  if (item) {
    // Clone pour édition
    editingItem.value = JSON.parse(JSON.stringify(item))

    // Transformation des tableaux en string pour l'affichage (ex: ["Vue", "Java"] -> "Vue, Java")
    if (type === 'project' && Array.isArray(editingItem.value.technologies)) {
      editingItem.value.technologies = editingItem.value.technologies.join(', ')
    }
    if (type === 'experience' && Array.isArray(editingItem.value.toolsUsed)) {
      editingItem.value.toolsUsed = editingItem.value.toolsUsed.join(', ')
    }
    if (type === 'education' && Array.isArray(editingItem.value.relevantCourses)) {
      editingItem.value.relevantCourses = editingItem.value.relevantCourses.join(', ')
    }
  } else {
    // Initialisation objets vides
    editingItem.value = {}
    if (type === 'skill') editingItem.value.level = 'Intermédiaire'
  }
  isModalOpen.value = true
}

const closeModal = () => {
  isModalOpen.value = false
  editingItem.value = {}
}

// --- LOGIQUE DE SAUVEGARDE ---
const handleSave = async () => {
  try {
    const item = { ...editingItem.value }

    // Re-transformation des Strings en Tableaux pour l'API
    if (modalType.value === 'project' && typeof item.technologies === 'string') {
      item.technologies = item.technologies
        .split(',')
        .map((t: string) => t.trim())
        .filter((t: string) => t)
    }
    if (modalType.value === 'experience' && typeof item.toolsUsed === 'string') {
      item.toolsUsed = item.toolsUsed
        .split(',')
        .map((t: string) => t.trim())
        .filter((t: string) => t)
    }
    if (modalType.value === 'education' && typeof item.relevantCourses === 'string') {
      item.relevantCourses = item.relevantCourses
        .split(',')
        .map((t: string) => t.trim())
        .filter((t: string) => t)
    }

    // API CALLS
    if (modalType.value === 'project') {
      item.id
        ? await portfolioService.updateProject(item.id, item)
        : await portfolioService.addProject(item)
    } else if (modalType.value === 'experience') {
      item.id
        ? await portfolioService.updateExperience(item.id, item)
        : await portfolioService.addExperience(item)
    } else if (modalType.value === 'education') {
      item.id
        ? await portfolioService.updateEducation(item.id, item)
        : await portfolioService.addEducation(item)
    } else if (modalType.value === 'skill') {
      item.id
        ? await portfolioService.updateSkill(item.id, item)
        : await portfolioService.addSkill(item)
    }

    await loadData()
    closeModal()
  } catch (error) {
    console.error('Erreur sauvegarde', error)
    alert('Erreur lors de la sauvegarde')
  }
}

// --- LOGIQUE SUPPRESSION ---
const handleDelete = async (type: string, id: number) => {
  if (!confirm('Supprimer cet élément ?')) return
  try {
    if (type === 'project') await portfolioService.deleteProject(id)
    if (type === 'experience') await portfolioService.deleteExperience(id)
    if (type === 'education') await portfolioService.deleteEducation(id)
    if (type === 'skill') await portfolioService.deleteSkill(id)
    await loadData()
  } catch (error) {
    console.error('Erreur suppression', error)
  }
}

// --- SAUVEGARDE PROFIL ---
const saveProfile = async () => {
  if (!portfolio.value) return
  try {
    await portfolioService.updateProfile(portfolio.value.profile)
    alert('Profil mis à jour !')
  } catch (error) {
    alert('Erreur update profil')
  }
}

// --- UPLOAD FICHIER (PROFIL) ---
const handleProfileUpload = async (event: Event, targetField: 'avatarUrl' | 'cvPdfUrl') => {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (!file || !portfolio.value) return
  try {
    const response = await portfolioService.uploadFile(file)
    const fileName = typeof response === 'string' ? response : response.fileName || response.url
    portfolio.value.profile[targetField] = fileName
    await saveProfile()
  } catch (error) {
    alert('Erreur upload')
  }
}

// --- UPLOAD FICHIER (ITEM MODAL) ---
// Sert pour l'image du projet ou le logo de l'école
const handleItemUpload = async (event: Event, targetField: string) => {
  const file = (event.target as HTMLInputElement).files?.[0]
  if (!file) return
  try {
    const response = await portfolioService.uploadFile(file)
    const fileName = typeof response === 'string' ? response : response.fileName || response.url
    editingItem.value[targetField] = fileName
  } catch (error) {
    alert('Erreur upload item')
  }
}

const getFileUrl = (path: string) => {
  if (!path) return ''
  return path.startsWith('http') ? path : `http://localhost:8080/api/files/${path}`
}
</script>

<template>
  <div class="min-h-screen bg-[#050505] text-gray-300 font-sans selection:bg-emerald-500/30 pb-20">
    <header class="bg-[#0a0a0a] border-b border-white/5 sticky top-0 z-40 shadow-md">
      <div class="max-w-7xl mx-auto px-6 h-16 flex items-center justify-between">
        <div class="flex items-center gap-4">
          <a
            href="/"
            class="p-2 hover:bg-white/5 rounded-lg transition-colors text-gray-400 hover:text-white"
            title="Retour au site"
          >
            <ArrowLeft :size="20" />
          </a>
          <h1 class="text-xl font-bold font-mono tracking-tighter text-white">
            ADMIN<span class="text-emerald-500">_PANEL</span>
          </h1>
        </div>
        <div class="text-xs font-mono text-gray-500">SYSTEM_READY</div>
      </div>
    </header>

    <div v-if="loading" class="flex justify-center items-center h-[calc(100vh-64px)]">
      <span class="animate-pulse text-emerald-500 font-mono">LOADING_DATA...</span>
    </div>

    <main v-else-if="portfolio" class="max-w-7xl mx-auto px-6 py-8">
      <div class="flex flex-wrap gap-2 mb-8 border-b border-white/5 pb-1 overflow-x-auto">
        <button
          @click="activeTab = 'profile'"
          :class="['tab-btn', activeTab === 'profile' ? 'active' : '']"
        >
          <User :size="16" /> Profil
        </button>
        <button
          @click="activeTab = 'projects'"
          :class="['tab-btn', activeTab === 'projects' ? 'active' : '']"
        >
          <Briefcase :size="16" /> Projets
        </button>
        <button
          @click="activeTab = 'experiences'"
          :class="['tab-btn', activeTab === 'experiences' ? 'active' : '']"
        >
          <Layers :size="16" /> Expériences
        </button>
        <button
          @click="activeTab = 'educations'"
          :class="['tab-btn', activeTab === 'educations' ? 'active' : '']"
        >
          <GraduationCap :size="16" /> Formation
        </button>
        <button
          @click="activeTab = 'skills'"
          :class="['tab-btn', activeTab === 'skills' ? 'active' : '']"
        >
          <Code :size="16" /> Skills
        </button>
      </div>

      <div v-if="activeTab === 'profile'" class="grid grid-cols-1 md:grid-cols-3 gap-8">
        <div class="space-y-6">
          <div class="admin-card text-center">
            <h3 class="text-white font-bold mb-4">Avatar</h3>
            <img
              :src="
                portfolio.profile.avatarUrl
                  ? getFileUrl(portfolio.profile.avatarUrl)
                  : 'https://via.placeholder.com/150'
              "
              class="w-32 h-32 rounded-full mx-auto mb-4 object-cover border-2 border-emerald-500/20"
            />
            <label class="btn-secondary w-full cursor-pointer">
              <Upload :size="16" /> Changer Avatar
              <input
                type="file"
                class="hidden"
                @change="(e) => handleProfileUpload(e, 'avatarUrl')"
                accept="image/*"
              />
            </label>
          </div>

          <div class="admin-card text-center">
            <h3 class="text-white font-bold mb-4">CV (PDF)</h3>
            <div
              class="bg-white/5 p-4 rounded-lg mb-4 flex items-center justify-center text-emerald-500"
            >
              <FileText :size="32" />
            </div>
            <p class="text-xs text-gray-500 mb-4 truncate">
              {{ portfolio.profile.cvPdfUrl || 'Aucun CV' }}
            </p>
            <label class="btn-secondary w-full cursor-pointer">
              <Upload :size="16" /> Upload PDF
              <input
                type="file"
                class="hidden"
                @change="(e) => handleProfileUpload(e, 'cvPdfUrl')"
                accept="application/pdf"
              />
            </label>
          </div>
        </div>

        <div class="md:col-span-2 admin-card space-y-4">
          <div class="grid grid-cols-2 gap-4">
            <div class="input-group">
              <label>Prénom</label
              ><input v-model="portfolio.profile.firstName" class="input-field" />
            </div>
            <div class="input-group">
              <label>Nom</label><input v-model="portfolio.profile.lastName" class="input-field" />
            </div>
          </div>
          <div class="input-group">
            <label>Titre</label><input v-model="portfolio.profile.title" class="input-field" />
          </div>
          <div class="input-group">
            <label>Bio</label
            ><textarea v-model="portfolio.profile.bio" rows="4" class="input-field"></textarea>
          </div>

          <div class="grid grid-cols-2 gap-4">
            <div class="input-group">
              <label>Email</label><input v-model="portfolio.profile.email" class="input-field" />
            </div>
            <div class="input-group">
              <label>Téléphone</label
              ><input v-model="portfolio.profile.phone" class="input-field" />
            </div>
            <div class="input-group">
              <label>Ville</label><input v-model="portfolio.profile.location" class="input-field" />
            </div>
            <div class="input-group">
              <label>Langue 1</label
              ><input v-model="portfolio.profile.firstLanguage" class="input-field" />
            </div>
            <div class="input-group">
              <label>Langue 2</label
              ><input v-model="portfolio.profile.secondLanguage" class="input-field" />
            </div>
          </div>

          <h4
            class="text-emerald-500 font-mono text-sm mt-4 uppercase border-b border-white/5 pb-2"
          >
            Réseaux
          </h4>
          <div class="grid grid-cols-1 gap-3">
            <div class="input-group">
              <label>GitHub</label
              ><input v-model="portfolio.profile.githubUrl" class="input-field" />
            </div>
            <div class="input-group">
              <label>LinkedIn</label
              ><input v-model="portfolio.profile.linkedinUrl" class="input-field" />
            </div>
            <div class="input-group">
              <label>TryHackMe</label
              ><input v-model="portfolio.profile.tryHackMeUrl" class="input-field" />
            </div>
          </div>

          <div class="flex justify-end mt-6">
            <button @click="saveProfile" class="btn-primary">
              <Save :size="18" /> Sauvegarder Profil
            </button>
          </div>
        </div>
      </div>

      <div v-else-if="activeTab === 'projects'" class="space-y-6">
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold text-white">Projets</h2>
          <button @click="openModal('project')" class="btn-primary">
            <Plus :size="18" /> Nouveau
          </button>
        </div>
        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div
            v-for="project in portfolio.projects"
            :key="project.id"
            class="admin-card group hover:border-emerald-500/50"
          >
            <div class="flex justify-between items-start mb-2">
              <div class="flex gap-3 items-center">
                <img
                  v-if="project.imageUrl"
                  :src="getFileUrl(project.imageUrl)"
                  class="w-10 h-10 rounded object-cover bg-white/5"
                />
                <h3 class="font-bold text-lg text-white">{{ project.title }}</h3>
              </div>
              <div class="flex gap-2 opacity-50 group-hover:opacity-100 transition-opacity">
                <button @click="openModal('project', project)" class="text-blue-400">
                  <Edit :size="18" />
                </button>
                <button @click="handleDelete('project', project.id!)" class="text-red-500">
                  <Trash2 :size="18" />
                </button>
              </div>
            </div>
            <p class="text-xs text-emerald-500 mb-2 font-mono" v-if="project.securityFocus">
              Sec: {{ project.securityFocus }}
            </p>
            <p class="text-sm text-gray-400 line-clamp-2">{{ project.summary }}</p>
          </div>
        </div>
      </div>

      <div v-else-if="activeTab === 'experiences'" class="space-y-6">
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold text-white">Expériences</h2>
          <button @click="openModal('experience')" class="btn-primary">
            <Plus :size="18" /> Nouvelle
          </button>
        </div>
        <div class="space-y-4">
          <div
            v-for="exp in portfolio.experiences"
            :key="exp.id"
            class="admin-card flex justify-between items-center group"
          >
            <div>
              <h3 class="font-bold text-white text-lg">{{ exp.companyName }}</h3>
              <p class="text-emerald-500">
                {{ exp.position }}
                <span class="text-gray-500 text-xs ml-2">({{ exp.location }})</span>
              </p>
              <p class="text-xs text-gray-500">
                {{ exp.startDate }} - {{ exp.endDate || 'Présent' }}
              </p>
            </div>
            <div class="flex gap-3 opacity-50 group-hover:opacity-100">
              <button @click="openModal('experience', exp)" class="text-blue-400">
                <Edit :size="18" />
              </button>
              <button @click="handleDelete('experience', exp.id!)" class="text-red-500">
                <Trash2 :size="18" />
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="activeTab === 'educations'" class="space-y-6">
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold text-white">Formation</h2>
          <button @click="openModal('education')" class="btn-primary">
            <Plus :size="18" /> Nouvelle
          </button>
        </div>
        <div class="space-y-4">
          <div
            v-for="edu in portfolio.educations"
            :key="edu.id"
            class="admin-card flex justify-between items-center group"
          >
            <div class="flex gap-4 items-center">
              <img
                v-if="edu.logo"
                :src="getFileUrl(edu.logo)"
                class="w-12 h-12 object-contain bg-white/5 rounded p-1"
              />
              <div>
                <h3 class="font-bold text-white text-lg">{{ edu.schoolName }}</h3>
                <p class="text-emerald-500">{{ edu.degree }}</p>
                <p class="text-xs text-gray-500">
                  {{ edu.startDate }} - {{ edu.endDate }} | {{ edu.location }}
                </p>
              </div>
            </div>
            <div class="flex gap-3 opacity-50 group-hover:opacity-100">
              <button @click="openModal('education', edu)" class="text-blue-400">
                <Edit :size="18" />
              </button>
              <button @click="handleDelete('education', edu.id!)" class="text-red-500">
                <Trash2 :size="18" />
              </button>
            </div>
          </div>
        </div>
      </div>

      <div v-else-if="activeTab === 'skills'" class="space-y-6">
        <div class="flex justify-between items-center">
          <h2 class="text-2xl font-bold text-white">Skills</h2>
          <button @click="openModal('skill')" class="btn-primary">
            <Plus :size="18" /> Nouveau
          </button>
        </div>
        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
          <div
            v-for="skill in portfolio.skills"
            :key="skill.id"
            class="admin-card flex justify-between items-center group"
          >
            <div>
              <p class="font-bold text-white">{{ skill.name }}</p>
              <p class="text-[10px] text-gray-500">{{ skill.category }}</p>
            </div>
            <div class="flex gap-2 opacity-50 group-hover:opacity-100">
              <button @click="openModal('skill', skill)" class="text-blue-400">
                <Edit :size="14" />
              </button>
              <button @click="handleDelete('skill', skill.id!)" class="text-red-500">
                <Trash2 :size="14" />
              </button>
            </div>
          </div>
        </div>
      </div>
    </main>

    <div
      v-if="isModalOpen"
      class="fixed inset-0 z-50 flex items-center justify-center bg-black/80 backdrop-blur-sm p-4"
    >
      <div
        class="bg-[#0f0f0f] border border-white/10 rounded-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto shadow-2xl"
      >
        <div
          class="p-6 border-b border-white/5 flex justify-between items-center sticky top-0 bg-[#0f0f0f] z-10"
        >
          <h3 class="text-xl font-bold text-white uppercase">
            {{ editingItem.id ? 'Modifier' : 'Ajouter' }} {{ modalType }}
          </h3>
          <button @click="closeModal" class="text-gray-500 hover:text-white">
            <X :size="24" />
          </button>
        </div>

        <div class="p-6 space-y-5">
          <div v-if="modalType === 'project'" class="space-y-4">
            <div class="input-group">
              <label>Titre</label><input v-model="editingItem.title" class="input-field" />
            </div>

            <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
              <div class="input-group">
                <label>Résumé court</label
                ><textarea v-model="editingItem.summary" rows="3" class="input-field"></textarea>
              </div>
              <div class="input-group">
                <label>Image Projet</label>
                <div
                  class="border border-white/10 border-dashed rounded-lg p-4 text-center hover:bg-white/5 transition-colors cursor-pointer relative"
                >
                  <input
                    type="file"
                    class="absolute inset-0 opacity-0 cursor-pointer"
                    @change="(e) => handleItemUpload(e, 'imageUrl')"
                  />
                  <p class="text-xs text-gray-400" v-if="!editingItem.imageUrl">Click to Upload</p>
                  <img
                    v-else
                    :src="getFileUrl(editingItem.imageUrl)"
                    class="h-16 mx-auto object-contain"
                  />
                </div>
              </div>
            </div>

            <div class="input-group">
              <label>Description Complète</label
              ><textarea
                v-model="editingItem.fullDescription"
                rows="5"
                class="input-field"
              ></textarea>
            </div>

            <div class="input-group">
              <label>Focus Sécurité</label
              ><input
                v-model="editingItem.securityFocus"
                class="input-field"
                placeholder="Ex: Pentest, Secure Code..."
              />
            </div>

            <div class="input-group">
              <label>Technos (virgules)</label
              ><input
                v-model="editingItem.technologies"
                class="input-field"
                placeholder="Java, Spring, Vue..."
              />
            </div>

            <div class="grid grid-cols-2 gap-4">
              <div class="input-group">
                <label>GitHub URL</label
                ><input v-model="editingItem.githubUrl" class="input-field" />
              </div>
              <div class="input-group">
                <label>Demo URL</label><input v-model="editingItem.demoUrl" class="input-field" />
              </div>
            </div>
          </div>

          <div v-if="modalType === 'experience'" class="space-y-4">
            <div class="input-group">
              <label>Entreprise</label
              ><input v-model="editingItem.companyName" class="input-field" />
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div class="input-group">
                <label>Poste</label><input v-model="editingItem.position" class="input-field" />
              </div>
              <div class="input-group">
                <label>Lieu</label><input v-model="editingItem.location" class="input-field" />
              </div>
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div class="input-group">
                <label>Début</label><input v-model="editingItem.startDate" class="input-field" />
              </div>
              <div class="input-group">
                <label>Fin</label><input v-model="editingItem.endDate" class="input-field" />
              </div>
            </div>
            <div class="input-group">
              <label>Description</label
              ><textarea v-model="editingItem.description" rows="4" class="input-field"></textarea>
            </div>
            <div class="input-group">
              <label>Outils (virgules)</label
              ><input v-model="editingItem.toolsUsed" class="input-field" />
            </div>
          </div>

          <div v-if="modalType === 'education'" class="space-y-4">
            <div class="flex gap-4">
              <div class="w-full input-group">
                <label>École</label><input v-model="editingItem.schoolName" class="input-field" />
              </div>
              <div class="w-1/3 input-group">
                <label>Logo</label>
                <div
                  class="border border-white/10 border-dashed rounded-lg h-[42px] flex items-center justify-center cursor-pointer relative hover:bg-white/5"
                >
                  <input
                    type="file"
                    class="absolute inset-0 opacity-0 cursor-pointer"
                    @change="(e) => handleItemUpload(e, 'logo')"
                  />
                  <ImageIcon v-if="!editingItem.logo" :size="16" class="text-gray-500" />
                  <img v-else :src="getFileUrl(editingItem.logo)" class="h-8 object-contain" />
                </div>
              </div>
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div class="input-group">
                <label>Diplôme</label><input v-model="editingItem.degree" class="input-field" />
              </div>
              <div class="input-group">
                <label>Filière</label
                ><input v-model="editingItem.fieldOfStudy" class="input-field" />
              </div>
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div class="input-group">
                <label>Début</label><input v-model="editingItem.startDate" class="input-field" />
              </div>
              <div class="input-group">
                <label>Fin</label><input v-model="editingItem.endDate" class="input-field" />
              </div>
            </div>
            <div class="grid grid-cols-2 gap-4">
              <div class="input-group">
                <label>Lieu</label><input v-model="editingItem.location" class="input-field" />
              </div>
              <div class="input-group">
                <label>Site Web (URL)</label><input v-model="editingItem.url" class="input-field" />
              </div>
            </div>
            <div class="input-group">
              <label>Description</label
              ><textarea v-model="editingItem.description" rows="3" class="input-field"></textarea>
            </div>
            <div class="input-group">
              <label>Cours Pertinents (virgules)</label
              ><input
                v-model="editingItem.relevantCourses"
                class="input-field"
                placeholder="Algo, Réseau, BDD..."
              />
            </div>
          </div>

          <div v-if="modalType === 'skill'" class="space-y-4">
            <div class="input-group">
              <label>Nom</label><input v-model="editingItem.name" class="input-field" />
            </div>
            <div class="input-group">
              <label>Catégorie</label><input v-model="editingItem.category" class="input-field" />
            </div>
            <div class="input-group">
              <label>Icon URL (Lucide name or URL)</label
              ><input v-model="editingItem.iconUrl" class="input-field" />
            </div>
            <div class="input-group">
              <label>Niveau</label>
              <select v-model="editingItem.level" class="input-field bg-[#050505]">
                <option>Débutant</option>
                <option>Intermédiaire</option>
                <option>Avancé</option>
                <option>Expert</option>
              </select>
            </div>
          </div>
        </div>

        <div
          class="p-6 border-t border-white/5 flex justify-end gap-3 bg-[#0f0f0f] sticky bottom-0"
        >
          <button
            @click="closeModal"
            class="px-4 py-2 text-gray-400 hover:text-white transition-colors"
          >
            Annuler
          </button>
          <button @click="handleSave" class="btn-primary"><Save :size="18" /> Enregistrer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
@reference "../assets/main.css";
.tab-btn {
  @apply flex items-center gap-2 px-4 py-3 text-sm font-mono text-gray-500 border-b-2 border-transparent transition-all hover:text-white whitespace-nowrap;
}
.tab-btn.active {
  @apply text-emerald-500 border-emerald-500 bg-emerald-500/5;
}

.admin-card {
  @apply bg-white/5 border border-white/5 p-6 rounded-2xl transition-all;
}

.input-group {
  @apply flex flex-col gap-1.5;
}
.input-group label {
  @apply text-[10px] font-mono uppercase text-gray-500 tracking-wider;
}
.input-field {
  @apply w-full bg-[#050505] border border-white/10 rounded-lg px-4 py-2.5 text-sm text-gray-200 focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 outline-none transition-all placeholder-gray-700;
}

.btn-primary {
  @apply flex items-center gap-2 bg-emerald-600 hover:bg-emerald-500 text-white px-4 py-2 rounded-lg font-bold text-sm transition-all shadow-lg shadow-emerald-900/20 active:scale-95;
}
.btn-secondary {
  @apply flex items-center justify-center gap-2 bg-white/5 hover:bg-white/10 text-gray-300 border border-white/10 px-4 py-2 rounded-lg text-xs font-mono uppercase tracking-wider transition-all;
}
</style>
