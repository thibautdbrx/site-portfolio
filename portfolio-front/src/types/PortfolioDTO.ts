export interface EducationDTO {
  id: number
  schoolName: string
  degree: string
  fieldOfStudy: string
  startDate: string
  endDate: string
  relevantCourses: string[]
  logo: string
  url: string
  description: string
  location: string
}

export interface ExperienceDTO {
  id: number
  companyName: string;
  position: string;
  location: string;
  startDate: string;
  endDate: string;
  description: string;
  toolsUsed: string[];
}

export interface PortfolioDTO {
  profile: ProfileDTO;
  projects: ProjectDTO[];
  skills: SkillDTO[];
  educations: EducationDTO[];
  experiences: ExperienceDTO[];
}

export interface ProjectDTO {
  id: number;
  title: string;
  summary: string;
  fullDescription: string;
  securityFocus: string;
  technologies: string[];
  githubUrl: string;
  demoUrl: string;
  imageUrl: string;
}

export interface ProfileDTO {
  id: number;
  firstName: string;
  lastName: string;
  title: string;
  phone: string;
  bio: string;
  email: string;
  location: string;
  firstLanguage: string;
  secondLanguage: string;
  photo: string;
  githubUrl: string;
  linkedinUrl: string;
  tryHackMeUrl: string;
  cvPdfUrl: string;
  avatarUrl: string;
}

export interface SkillDTO {
  id: number;
  name: string;
  category: string;
  level: string;
  iconUrl: string;
}