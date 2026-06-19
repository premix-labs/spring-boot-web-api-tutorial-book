// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';

const site = process.env.SITE ?? 'http://localhost:4321';
const base = process.env.BASE_PATH;

export default defineConfig({
	site,
	...(base ? { base } : {}),
	output: 'static',
	integrations: [
		starlight({
			title: 'Spring Boot Tutorial Book',
			description:
				'หนังสือสอน Spring Boot สำหรับมือใหม่ ผ่านโปรเจกต์ Login, Register และ Admin API',
			customCss: ['./src/styles/custom.css'],
			sidebar: [
				{
					label: 'เริ่มต้น',
					items: [
						{ label: 'หน้าแรก', slug: '' },
						{ label: 'แผนทั้งเล่ม', slug: 'book-plan' },
						{ label: 'สถานะต้นฉบับ', slug: 'manuscript-status' },
					],
				},
				{
					label: 'ภาค 1: ปูพื้นฐาน',
					items: [{ autogenerate: { directory: 'part-1-foundation' } }],
				},
				{
					label: 'ภาค 2: REST API',
					items: [{ autogenerate: { directory: 'part-2-rest-api' } }],
				},
				{
					label: 'ภาค 3: ฐานข้อมูล',
					items: [{ autogenerate: { directory: 'part-3-database' } }],
				},
				{
					label: 'ภาค 4: API ที่ดีขึ้น',
					items: [{ autogenerate: { directory: 'part-4-better-api' } }],
				},
				{
					label: 'ภาค 5: Login/Register',
					items: [{ autogenerate: { directory: 'part-5-auth' } }],
				},
				{
					label: 'ภาค 6: Admin System',
					items: [{ autogenerate: { directory: 'part-6-admin' } }],
				},
				{
					label: 'ภาค 7: Production Ready',
					items: [{ autogenerate: { directory: 'part-7-production-ready' } }],
				},
				{
					label: 'ภาคผนวก',
					items: [{ autogenerate: { directory: 'appendix' } }],
				},
			],
		}),
	],
});
