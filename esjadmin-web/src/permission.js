import { router, notFoundRoutes } from './router'
import store from './store'
import { Message } from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'

import Layout from '@/layout'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/auth-redirect'] // no redirect whitelist

function transformMenu(menus) {
  const menusMap = []
  menus.map(v => {
    const { type, path, name, component, children, meta, hidden } = v
    // 重新构建路由对象
    const item = {
      path,
      name: name || 'Menu' + v.id,
      // component: children.length > 0 ? Layout : () => import(`@/views/${component}`),
      // component: component == 'Layout' ? Layout : () => import(`@/views/${component}`),
      hidden: hidden,
      meta,
      children: transformMenu(children)
    }

    // if (children.length > 0) {
    //   item.redirect = 'noRedirect'
    // }

    if (type.toString() === '0') {
      if (children.length < 1) {
        return
      }
      item.component = Layout
    } else if (component) {
      if (component === 'Layout') {
        item.component = Layout
      } else {
        item.component = (resolve) => require([`@/views/${component}`], resolve)
      }
    }

    menusMap.push(item)
  })
  return menusMap
}

router.beforeEach(async(to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()

  if (hasToken) {
    if (to.path === '/login') {
      // if is logged in, redirect to the home page
      next({ path: '/' })
      NProgress.done() // hack: https://github.com/PanJiaChen/vue-element-admin/pull/2939
    } else {
      const hasGetUserInfo = store.getters.username
      if (hasGetUserInfo) {
        next()
      } else {
        try {
          // get user info
          await store.dispatch('user/getInfo')
          // 添加动态菜单
          const menus = store.getters.menus
          let addRoutes = transformMenu(menus)
          addRoutes = addRoutes.concat(notFoundRoutes)

          router.options.routes = router.options.routes.concat(addRoutes)
          router.addRoutes(addRoutes)

          await store.dispatch('permission/addRoutes', addRoutes)

          // next()
          next({ ...to, replace: true }) // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
