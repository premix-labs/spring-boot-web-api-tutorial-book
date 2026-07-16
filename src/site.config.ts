// site.config.ts — Single place to rebrand this template for this book.
// Everything here flows into the landing page and the reading layout.

export const siteConfig = {
  /** Short brand name shown in the header logo lockup. */
  title: 'Spring Boot',
  /** Small line under the title in the header. */
  tagline: 'Backend API Tutorial',
  /** Used for <meta description>, OG description and the header pill. */
  description: 'หนังสือสอน Spring Boot สำหรับมือใหม่ ผ่านโปรเจกต์ Login, Register และ Admin API',
  /** HTML lang attribute — Thai, since chapter content is authored in Thai. */
  lang: 'th',
  /**
   * Social share image for link previews (og:image / Twitter card).
   * Drop a 1200×630 PNG in `public/` and point here, e.g. '/og-image.png'.
   * Leave empty to omit the tags. Resolved against `site` in astro.config.mjs.
   */
  ogImage: '',
  /** Theme applied on first visit, before the user picks one. 'dark' | 'light' */
  defaultTheme: 'dark' as 'dark' | 'light',
  /** GitHub repo URL. Leave empty to hide the header GitHub button. */
  github: '',

  hero: {
    eyebrow: 'Spring Boot · REST API · PostgreSQL',
    /** Rendered as: {prefix} <accent>{accent}</accent> {suffix} */
    prefix: 'สร้าง',
    accent: 'Backend API',
    suffix: 'ด้วย Spring Boot',
    subtitle:
      'เรียนรู้ Spring Boot จากศูนย์ผ่านโปรเจกต์ Backend API จริง — REST API, PostgreSQL, Login/Register ด้วย JWT, Admin System จนถึง Production Ready',
  },

  /** Extra stat pills shown under the hero, after the auto-computed chapter count. */
  extraStats: [
    { value: '7', label: 'Parts' },
    { value: 'JWT', label: 'Auth' },
  ],

  features: [
    {
      icon: 'server',
      title: 'REST API ที่ใช้งานได้จริง',
      desc: 'Controller, CRUD, layered architecture และ Postman testing',
    },
    { icon: 'database', title: 'PostgreSQL + JPA', desc: 'เชื่อมต่อฐานข้อมูลจริงด้วย Spring Data JPA' },
    { icon: 'shield-check', title: 'Auth ด้วย JWT', desc: 'Register, login, current user และ protect endpoint' },
    { icon: 'search', title: 'ค้นหาได้ทั้งเล่ม', desc: 'Full-text search แบบออฟไลน์ผ่าน Pagefind' },
  ],

  footer: {
    text: 'Open Source',
  },
} as const;
