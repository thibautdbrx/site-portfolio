import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
import { jwtDecode } from 'jwt-decode'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    { path: '/', name: 'home', component: HomeView},
    { path: '/admin', name: 'admin', component: () => import('@/views/AdminView.vue')},
    { path: '/login', name: 'login', component: () => import('@/views/LoginView.vue')},
  ],
})

router.beforeEach((to, from, next) => {
  // 1. Récupérer le cookie 'token'
  const token = document.cookie
    .split('; ')
    .find((row) => row.startsWith('token='))
    ?.split('=')[1]

  // 2. Vérifier si on essaie d'accéder à l'admin
  if (to.path.startsWith('/admin')) {
    if (!token) {
      return next('/login')
    }

    try {
      // 3. Décoder le token pour vérifier l'expiration
      const decoded: any = jwtDecode(token)
      const currentTime = Date.now() / 1000

      if (decoded.exp < currentTime) {
        // Token expiré : on nettoie le cookie et on redirige
        document.cookie = 'token=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;'
        return next('/login')
      }

      // Tout est bon !
      next()
    } catch (e) {
      // Token invalide (malformé)
      next('/login')
    }
  } else {
    // Page publique
    next()
  }
})

export default router
